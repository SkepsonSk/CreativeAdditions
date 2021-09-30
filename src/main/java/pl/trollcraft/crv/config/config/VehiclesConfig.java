package pl.trollcraft.crv.config.config;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import pl.trollcraft.crv.config.ConfigProvider;
import pl.trollcraft.crv.help.Help;
import pl.trollcraft.crv.vehicles.model.AbstractVehicle;
import pl.trollcraft.crv.vehicles.service.VehiclesService;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class VehiclesConfig implements Config {

    private static final Logger LOG = Logger.getLogger(VehiclesConfig.class.getSimpleName());

    private final VehiclesService vehiclesService;

    public VehiclesConfig(VehiclesService vehiclesService) {
        this.vehiclesService = vehiclesService;
    }

    @Override
    public void configure(ConfigProvider provider) {

        ConfigurationSection playerSection = provider.conf().getConfigurationSection("vehicles");

        if (playerSection == null) {
            return;
        }

        playerSection.getKeys(false).forEach( playerName -> {

            ConfigurationSection vehiclesSection = provider.conf()
                    .getConfigurationSection(String.format("vehicles.%s", playerName));

            vehiclesSection.getKeys(false).forEach( vehicleUUID -> {

                List<String> parts = provider.conf().getStringList(String.format("vehicles.%s.%s", playerName, vehicleUUID));
                List<Help.ArmorStandLocation> partsLocations = parts.stream().map(Help::armorStandLocationFromString).collect(Collectors.toList());
                List<ArmorStand> partsArmorStands = partsLocations.stream()
                        .map( armorStandLocation -> {

                            Location loc = armorStandLocation.location;
                            String name = armorStandLocation.name;

                            Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, 0.25, 0.25, 0.25);
                            if (entities.isEmpty()) {
                                LOG.warning("Cannot find part in the world.");
                                return null;
                            }
                            else {
                                LOG.info("Matching parts: " + entities.size());

                                for (Entity e : entities) {
                                    if (e.getName().equals(name)) {
                                        return (ArmorStand) e;
                                    }
                                }

                                return null;
                            }

                        } ).collect(Collectors.toList());

                AbstractVehicle abstractVehicle = new AbstractVehicle(partsArmorStands, playerName, UUID.fromString(vehicleUUID));
                vehiclesService.register(abstractVehicle);
            } );

        } );

    }

}
