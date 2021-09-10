package pl.trollcraft.crv.vehicles.model;

public class VehicleCategory {

    private final String id;
    private final String display;
    private final int slot;

    public VehicleCategory(String id, String display, int slot) {
        this.id = id;
        this.display = display;
        this.slot = slot;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public int getSlot() {
        return slot;
    }
}
