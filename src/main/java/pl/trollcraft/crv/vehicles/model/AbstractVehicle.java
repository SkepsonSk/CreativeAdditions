package pl.trollcraft.crv.vehicles.model;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class AbstractVehicle {

    private final Collection<ArmorStand> parts;
    private final String playerName;
    private final UUID vehicleUUID;

    public AbstractVehicle(Collection<ArmorStand> parts, String playerName, UUID vehicleUUID) {
        this.parts = parts;
        this.playerName = playerName;
        this.vehicleUUID = vehicleUUID;
    }

    public Collection<ArmorStand> getParts() {
        return parts;
    }

    public UUID getVehicleUUID() {
        return vehicleUUID;
    }

    public Location getLocation() {
        Optional<ArmorStand> part = this.parts.stream().findAny();
        return part.map(Entity::getLocation).orElse(null);
    }

    public String getPlayerName() {
        return playerName;
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
