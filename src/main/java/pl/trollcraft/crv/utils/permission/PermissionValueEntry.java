package pl.trollcraft.crv.utils.permission;

public class PermissionValueEntry<T> implements Comparable<PermissionValueEntry<T>> {

    private final int order;
    private final String permission;
    private final T value;

    public PermissionValueEntry(int order, String permission, T value) {
        this.order = order;
        this.permission = permission;
        this.value = value;
    }

    public int getOrder() {
        return order;
    }

    public String getPermission() {
        return permission;
    }

    public T getValue() {
        return value;
    }

    @Override
    public int compareTo(PermissionValueEntry<T> o) {
        return getOrder() - o.getOrder();
    }
}
