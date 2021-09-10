package pl.trollcraft.crv.inventoryClear.clearRules;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.trollcraft.crv.inventoryClear.ClearRule;

public class VehicleClearRule implements ClearRule {

    @Override
    public boolean shouldClearItem(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }

        Material material = itemStack.getType();
        if (material != Material.CHEST) {
            return true;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return true;
        }

        String displayName = itemMeta.getDisplayName();
        return !displayName.contains("§");
    }
}
