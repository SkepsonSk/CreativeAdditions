package pl.trollcraft.crv.inventoryClear;

import org.bukkit.inventory.ItemStack;

public interface ClearRule {
    boolean shouldClearItem(ItemStack itemStack);
}
