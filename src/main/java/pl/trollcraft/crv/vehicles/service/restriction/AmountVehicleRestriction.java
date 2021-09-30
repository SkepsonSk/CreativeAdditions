package pl.trollcraft.crv.vehicles.service.restriction;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.trollcraft.crv.controller.DefaultConfigController;
import pl.trollcraft.crv.vehicles.service.VehiclesService;

public class AmountVehicleRestriction implements VehicleRestriction {

    private final DefaultConfigController defaultConfigController;
    private final VehiclesService vehiclesService;

    public AmountVehicleRestriction(DefaultConfigController defaultConfigController, VehiclesService vehiclesService) {
        this.defaultConfigController = defaultConfigController;
        this.vehiclesService = vehiclesService;
    }

    @Override
    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&',
                "&cPosiadasz zbyt wiele pojazdow.\nZnajdz swoje pojazdy uzywajac komendy " +
                        "&e/gdziePojazd");
    }

    @Override
    public boolean canSpawn(Player player) {
        int maximumVehiclesAmount = defaultConfigController.getMaximumVehicles().get(player);
        int spawnedVehiclesAmount = vehiclesService.getVehiclesAmountForPlayer(player);
        return maximumVehiclesAmount > spawnedVehiclesAmount;
    }
}
