package pl.trollcraft.crv.config.config;

import org.bukkit.configuration.ConfigurationSection;
import pl.trollcraft.crv.config.ConfigProvider;
import pl.trollcraft.crv.controller.DefaultConfigController;

public class DefaultConfig implements Config {

    private final DefaultConfigController defaultConfigController;

    public DefaultConfig(DefaultConfigController defaultConfigController) {
        this.defaultConfigController = defaultConfigController;
    }

    @Override
    public void configure(ConfigProvider provider) {

        ConfigurationSection maximumVehicles = provider.conf()
                .getConfigurationSection("vehicles.maximum-vehicles-spawned");

        maximumVehicles.getKeys(false).forEach( permission -> {
            int maximumVehiclesValue = provider
                    .read(String.format("vehicles.maximum-vehicles-spawned.%s", permission), Integer.class);
            defaultConfigController.getMaximumVehicles().insert(permission, maximumVehiclesValue);
        } );
    }

}
