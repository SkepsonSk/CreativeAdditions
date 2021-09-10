package pl.trollcraft.crv.vehicles.model;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.Optional;

public class AbstractVehicle {

    private final Collection<ArmorStand> parts;

    public AbstractVehicle(Collection<ArmorStand> parts) {
        this.parts = parts;
    }

    public Collection<ArmorStand> getParts() {
        return parts;
    }

    public Location getLocation() {
        Optional<ArmorStand> part = this.parts.stream().findAny();
        return part.map(Entity::getLocation).orElse(null);
    }

    @Override
    public String toString() {
        Location location = getLocation();
        if (location == null) {
            return "Unknown";
        }
        else{
            return "X:" + location.getBlockX() + ", Y:" + location.getBlockY() + ", Z:" + location.getBlockZ();
        }
    }
}
