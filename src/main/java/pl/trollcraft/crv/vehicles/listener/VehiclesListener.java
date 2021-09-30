package pl.trollcraft.crv.vehicles.listener;

import es.pollitoyeye.vehicles.events.VehiclePickupEvent;
import es.pollitoyeye.vehicles.events.VehiclePlaceEvent;
import es.pollitoyeye.vehicles.events.VehicleSpawnedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.trollcraft.crv.utils.Utils;
import pl.trollcraft.crv.vehicles.model.AbstractVehicle;
import pl.trollcraft.crv.vehicles.service.restriction.VehicleRestriction;
import pl.trollcraft.crv.vehicles.service.restriction.VehiclesRestrictionService;
import pl.trollcraft.crv.vehicles.service.VehiclesService;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class VehiclesListener implements Listener {

    private final VehiclesRestrictionService vehiclesRestrictionService;
    private final VehiclesService vehiclesService;

    public VehiclesListener(VehiclesRestrictionService vehiclesRestrictionService,
                            VehiclesService vehiclesService) {

        this.vehiclesRestrictionService = vehiclesRestrictionService;
        this.vehiclesService = vehiclesService;
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        vehiclesService.track(player);
    }

    @EventHandler
    public void onLeave (PlayerQuitEvent event) {
        Player player = event.getPlayer();
        vehiclesService.unTrack(player);
    }

    @EventHandler
    public void onCreate (VehiclePlaceEvent event) {
        Player player = event.getOwner();

        Optional<VehicleRestriction> oVehicleRestriction = vehiclesRestrictionService.canSpawnVehicle(player);
        if (!oVehicleRestriction.isEmpty()) {
            VehicleRestriction vehicleRestriction = oVehicleRestriction.get();
            player.sendMessage(vehicleRestriction.getMessage());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpawn(VehicleSpawnedEvent event) {
        Collection<ArmorStand> parts = event.getVehicleParts();

        UUID uuid = UUID.fromString(event.getOwner());
        Player player = Bukkit.getPlayer(uuid);
        UUID vehicleUUID = UUID.fromString(event.getVehicleParts().get(0).getName().split(";")[1]);

        AbstractVehicle vehicle = new AbstractVehicle(parts, player.getName(), vehicleUUID);
        vehiclesService.register(vehicle);

        if (player == null || !player.isOnline()) {
            Bukkit.getLogger().warning("No player under uuid of " + event.getOwner());
        }
        else {
            vehiclesService.track(player, vehicle);
        }

    }

    @EventHandler
    public void onRemove (VehiclePickupEvent event) {
        Player player = event.getPlayer();
        Optional<Entity> oEntity = event.getBlockLocation().getWorld().getNearbyEntities(event.getBlockLocation(), 0.5, 0.5, 0.5)
                .stream()
                .filter( ent -> ent instanceof ArmorStand )
                .filter( ent -> ent.getName().contains(";") )
                .findFirst();

        if (oEntity.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage("No entity found.");
            return;
        }

        Entity entity = oEntity.get();
        UUID vehicleUUID = UUID.fromString(entity.getName().split(";")[1]);

        Optional<AbstractVehicle> oVehicle = vehiclesService.findVehicleForPlayer(vehicleUUID);
        if (oVehicle.isPresent()) {
            AbstractVehicle vehicle = oVehicle.get();
            vehiclesService.unTrack(player, vehicle);
            vehiclesService.unregister(vehicle);
        }
        else {
            Bukkit.getConsoleSender().sendMessage("No vehicle found under id.");
        }

    }

}
