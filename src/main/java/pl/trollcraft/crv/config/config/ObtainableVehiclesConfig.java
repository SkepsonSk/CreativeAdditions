package pl.trollcraft.crv.config.config;

import es.pollitoyeye.vehicles.VehiclesMain;
import es.pollitoyeye.vehicles.enums.VehicleType;
import es.pollitoyeye.vehicles.interfaces.VehicleSubType;
import org.bukkit.ChatColor;
import pl.trollcraft.crv.config.ConfigProvider;
import pl.trollcraft.crv.vehicles.controller.VehiclesController;
import pl.trollcraft.crv.vehicles.model.ObtainableVehicle;
import pl.trollcraft.crv.vehicles.model.VehicleCategory;

public class ObtainableVehiclesConfig implements Config {

    private final VehiclesController vehiclesController;

    public ObtainableVehiclesConfig(VehiclesController vehiclesController) {
        this.vehiclesController = vehiclesController;
    }

    @Override
    public void configure(ConfigProvider provider) {
        configureCategories(provider);
        configureVehicles(provider);
    }

    private void configureCategories(ConfigProvider provider) {
        provider.conf().getConfigurationSection("categories").getKeys(false).forEach( id -> {

            String display = ChatColor.translateAlternateColorCodes('&',
                    provider.read(String.format("categories.%s.display", id), String.class));
            int slot = provider.read(String.format("categories.%s.slot", id), Integer.class);

            VehicleCategory vehicleCategory = new VehicleCategory(id, display, slot);
            vehiclesController.register(vehicleCategory);

        });
    }

    private void configureVehicles(ConfigProvider provider) {
        provider.conf().getConfigurationSection("vehicles").getKeys(false).forEach( id -> {

            String display = ChatColor.translateAlternateColorCodes('&',
                    provider.read(String.format("vehicles.%s.display", id), String.class));
            VehicleType type = VehicleType.valueOf(provider.read(String.format("vehicles.%s.type", id), String.class));
            VehicleSubType subType = VehiclesMain.getPlugin()
                    .vehicleSubTypeFromString(type, provider.read(String.format("vehicles.%s.subType", id), String.class));
            double price = provider.read(String.format("vehicles.%s.price", id), Double.class);
            int slot = provider.read(String.format("vehicles.%s.slot", id), Integer.class);
            String vehicleCategory = provider.read(String.format("vehicles.%s.category", id), String.class);

            ObtainableVehicle vehicle = new ObtainableVehicle(id, display, type, subType, price, slot, vehicleCategory);
            vehiclesController.register(vehicle);

        } );
    }

}
