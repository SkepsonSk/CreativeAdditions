package pl.trollcraft.crv.vehicles.service.restriction;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehiclesRestrictionService {

    private final List<VehicleRestriction> restrictionList;

    public VehiclesRestrictionService() {
        this.restrictionList = new ArrayList<>();
    }

    public void register(VehicleRestriction vehicleRestriction) {
        this.restrictionList.add(vehicleRestriction);
    }

    public Optional<VehicleRestriction> canSpawnVehicle(Player player) {
        return restrictionList.stream()
                .filter(vehicleRestriction ->  !vehicleRestriction.canSpawn(player) )
                .findFirst();
    }



}
