package pl.trollcraft.crv.inventoryClear;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ClearCommand implements CommandExecutor {

    private final List<ClearRule> clearRuleList;

    public ClearCommand() {
        this.clearRuleList = new ArrayList<>();
    }

    public void registerRule(ClearRule clearRule) {
        this.clearRuleList.add(clearRule);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Komenda jedynie dla graczy online.");
            return true;
        }

        Player player = (Player) sender;
        this.clearInventory(player);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7Wyczyszczono EQ."));

        return true;
    }

    private void clearInventory(Player player) {
        Inventory inventory = player.getInventory();
        int size = inventory.getSize();

        for (int i = 0 ; i < size ; i++) {

            ItemStack itemStack = inventory.getItem(i);
            boolean shouldRemove = this.clearRuleList.stream()
                    .anyMatch( clearRule -> clearRule.shouldClearItem(itemStack) );

            if (shouldRemove) {
                inventory.setItem(i, null);
            }
        }
    }
}
