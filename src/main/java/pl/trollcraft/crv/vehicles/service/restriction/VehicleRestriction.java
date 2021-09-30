package pl.trollcraft.crv.vehicles.service.restriction;

import org.bukkit.entity.Player;

public interface VehicleRestriction {

    String getMessage();
    boolean canSpawn(Player player);

}
