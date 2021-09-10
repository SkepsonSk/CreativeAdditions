package pl.trollcraft.crv.vehicles.controller;

import pl.trollcraft.crv.vehicles.model.ObtainableVehicle;
import pl.trollcraft.crv.vehicles.model.VehicleCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VehiclesController {

    private final List<VehicleCategory> vehicleCategories;
    private final List<ObtainableVehicle> vehicles;

    public VehiclesController() {
        vehicleCategories = new ArrayList<>();
        vehicles = new ArrayList<>();
    }

    public void register(VehicleCategory vehicleCategory) {
        this.vehicleCategories.add(vehicleCategory);
    }

    public void register(ObtainableVehicle vehicle) {
        this.vehicles.add(vehicle);
    }

    public List<VehicleCategory> getVehicleCategories() {
        return vehicleCategories;
    }

    public List<ObtainableVehicle> getVehicles() {
        return vehicles;
    }

    public List<ObtainableVehicle> findByCategory(String vehicleCategoryName) {
        return vehicles.stream()
                .filter(vehicle -> vehicle.getVehicleCategory().equals(vehicleCategoryName))
                .collect(Collectors.toList());
    }

}
