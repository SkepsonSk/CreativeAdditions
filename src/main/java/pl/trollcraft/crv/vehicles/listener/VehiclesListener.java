package pl.trollcraft.crv.vehicles.listener;

import es.pollitoyeye.vehicles.events.VehicleSpawnedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.trollcraft.crv.vehicles.model.AbstractVehicle;
import pl.trollcraft.crv.vehicles.service.VehiclesService;
import java.util.Collection;
import java.util.UUID;

public class VehiclesListener implements Listener {

    private final VehiclesService vehiclesService;

    public VehiclesListener(VehiclesService vehiclesService) {
        this.vehiclesService = vehiclesService;
    }

    @EventHandler
    public void onSpawn(VehicleSpawnedEvent event) {

        Collection<ArmorStand> parts = event.getVehicleParts();

        UUID uuid = UUID.fromString(event.getOwner());
        Player player = Bukkit.getPlayer(uuid);

        AbstractVehicle vehicle = new AbstractVehicle(parts);
        vehiclesService.register(vehicle);

        if (player == null || !player.isOnline()) {
            Bukkit.getLogger().warning("No player under uuid of " + event.getOwner());
        }
        else {
            vehiclesService.track(player, vehicle);
        }

    }

}
