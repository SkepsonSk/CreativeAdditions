package pl.trollcraft.crv.utils.permission;

import org.bukkit.entity.Player;

import java.util.TreeSet;

public class PermissionValues<T> {

    private int order = 0;

    private final TreeSet<PermissionValueEntry<T>> groupedValues;

    public PermissionValues() {
        this.groupedValues = new TreeSet<>();
    }

    public void insert(String permission, T value) {
        PermissionValueEntry<T> permissionValueEntry = new PermissionValueEntry<>(order, permission, value);
        order++;
        this.groupedValues.add(permissionValueEntry);
    }

    public T get(Player player) {
        for (PermissionValueEntry<T> entry : groupedValues) {
            if (player.hasPermission(entry.getPermission())){
                return entry.getValue();
            }
        }
        throw new IllegalStateException("Player has not matching permission for the permission values.");
    }
}
