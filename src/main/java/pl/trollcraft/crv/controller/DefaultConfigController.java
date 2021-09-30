package pl.trollcraft.crv.controller;

import pl.trollcraft.crv.utils.permission.PermissionValues;

public class DefaultConfigController {

    private final PermissionValues<Integer> maximumVehicles;

    public DefaultConfigController() {
        this.maximumVehicles = new PermissionValues<>();
    }

    public PermissionValues<Integer> getMaximumVehicles() {
        return maximumVehicles;
    }
}
