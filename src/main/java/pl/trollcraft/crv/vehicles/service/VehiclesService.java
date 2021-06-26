package pl.trollcraft.crv.vehicles.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import pl.trollcraft.crv.config.ConfigProvider;
import pl.trollcraft.crv.help.Help;
import pl.trollcraft.crv.vehicles.model.AbstractVehicle;

import java.util.LinkedList;
import java.util.List;

public class VehiclesService {

    private final List<AbstractVehicle> vehicles;
    private final Multimap<OfflinePlayer, AbstractVehicle> playerSpawnedVehicles;
    private final ConfigProvider vehiclesProvider;

    public VehiclesService(ConfigProvider vehiclesProvider) {
        vehicles = new LinkedList<>();
        playerSpawnedVehicles = ArrayListMultimap.create();
        this.vehiclesProvider = vehiclesProvider;
    }

    public void register(AbstractVehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void unregister(AbstractVehicle vehicle) {
        vehicles.remove(vehicle);
    }

    public void track(Player player, AbstractVehicle vehicle) {
        playerSpawnedVehicles.put(player, vehicle);
    }

    public void unTrack(Player player, Vehicle vehicle) {
        playerSpawnedVehicles.remove(player, vehicle);
    }

    public void remove(OfflinePlayer player) {
        playerSpawnedVehicles.get(player).forEach(vehicle ->
            vehicle.getParts().forEach(Entity::remove)
        );

        playerSpawnedVehicles.removeAll(player);
    }

    public void removeAll() {
        vehicles.forEach(vehicle ->
            vehicle.getParts().forEach(Entity::remove)
        );

        vehicles.clear();
        playerSpawnedVehicles.clear();
    }

    public void save() {
        for (OfflinePlayer player : playerSpawnedVehicles.keys()) {
            int ind = 0;

            String name = player.getName();
            for (AbstractVehicle vehicle : playerSpawnedVehicles.get(player)) {
                vehiclesProvider.write(String.format("vehicles.%s.%d", name, ind), Help.toStringList(vehicle));
                ind++;
            }
        }
        vehiclesProvider.save();
    }

}
