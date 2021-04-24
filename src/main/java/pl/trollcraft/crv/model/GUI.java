package pl.trollcraft.crv.model;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GUI {

    private final Inventory inventory;
    private final Map<Integer, Consumer<InventoryClickEvent>> slotsMap;

    private final boolean cancelOnDefault;

    private boolean transition; // Informs if gui is currently transiting to other.

    public GUI(String title, int slots, boolean cancelOnDefault) {
        inventory = Bukkit.createInventory(null, slots, title);
        slotsMap = new HashMap<>();
        this.cancelOnDefault = cancelOnDefault;
        transition = false;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void add(ItemStack itemStack,
                    int slot,
                    Consumer<InventoryClickEvent> action) {

        inventory.setItem(slot, itemStack);

        if (slotsMap.containsKey(slot))
            slotsMap.replace(slot, action);
        else
            slotsMap.put(slot, action);
    }

    public void add(ItemStack itemStack,
                    int slot) {

        inventory.setItem(slot, itemStack);
        slotsMap.remove(slot);
    }

    public void reset(int slot) {
        inventory.setItem(slot, new ItemStack(Material.AIR));
        slotsMap.remove(slot);
    }

    public void click(int slot,
                      InventoryClickEvent event) {

        if (!slotsMap.containsKey(slot)) {

            if (cancelOnDefault)
                event.setCancelled(true);
            return;
        }

        Consumer<InventoryClickEvent> clickEvent = slotsMap.get(slot);
        clickEvent.accept(event);

    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void setTransiting(boolean transition) {
        this.transition = transition;
    }

    public boolean isTransiting() {
        return transition;
    }

}
