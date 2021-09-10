package pl.trollcraft.crv.vehicles.model;

import es.pollitoyeye.vehicles.enums.VehicleType;
import es.pollitoyeye.vehicles.interfaces.VehicleSubType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ObtainableVehicle {

    private final String id;
    private final String display;
    private final VehicleType type;
    private final VehicleSubType subType;
    private final double price;
    private final int slot;
    private final String vehicleCategory;

    public ObtainableVehicle(String id, String display, VehicleType type,
                             VehicleSubType subType, double price, int slot,
                             String vehicleCategory) {
        this.id = id;
        this.display = display;
        this.type = type;
        this.subType = subType;
        this.price = price;
        this.slot = slot;
        this.vehicleCategory = vehicleCategory;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public double getPrice() {
        return price;
    }

    public int getSlot() {
        return slot;
    }

    public String getVehicleCategory() {
        return vehicleCategory;
    }

    public void give(Player player) {
        type.getVehicleManager().giveItem(player, subType.getName(), new ItemStack[0]);
    }
}

