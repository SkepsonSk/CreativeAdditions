package pl.trollcraft.crv.vehicles.model;

import org.bukkit.entity.ArmorStand;

import java.util.Collection;

public class AbstractVehicle {

    private final Collection<ArmorStand> parts;

    public AbstractVehicle(Collection<ArmorStand> parts) {
        this.parts = parts;
    }

    public Collection<ArmorStand> getParts() {
        return parts;
    }
}
