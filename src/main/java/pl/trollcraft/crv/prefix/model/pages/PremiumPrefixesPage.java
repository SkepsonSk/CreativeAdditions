package pl.trollcraft.crv.prefix.model.pages;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.trollcraft.crv.prefix.controller.PrefixesController;
import pl.trollcraft.crv.prefix.datasource.PrefixUsersDataSource;
import pl.trollcraft.crv.prefix.model.PrefixUser;
import pl.trollcraft.crv.prefix.model.PrefixesGUI;
import pl.trollcraft.crv.prefix.model.prefix.Prefix;
import pl.trollcraft.crv.prefix.model.prefix.PrefixType;

import java.util.Arrays;
import java.util.function.Consumer;

public final class PremiumPrefixesPage extends PrefixesPage {

    public PremiumPrefixesPage(String name,
                               PrefixType type,
                               PrefixesGUI prefixesGUI,
                               PrefixesController prefixesController,
                               PrefixUser prefixUser,
                               PrefixUsersDataSource prefixUsersDataSource,
                               Economy economy) {

        super(name, type, prefixesGUI, prefixesController, prefixUser, prefixUsersDataSource, economy);
    }

    @Override
    protected void displayPrefix(Prefix prefix, int slot) {

        ItemStack is;
        ItemMeta meta;
        Consumer<InventoryClickEvent> click;

        if (prefixUser.getSelectedPrefix() != null && prefixUser.getSelectedPrefix().equals(prefix)) {

            is = new ItemStack(Material.ENCHANTED_BOOK);
            meta = is.getItemMeta();
            assert meta != null;
            meta.setDisplayName(ChatColor.GREEN + prefix.getName());
            meta.setLore(Arrays.asList("", ChatColor.translateAlternateColorCodes('&', "&ePrefix w uzyciu."),
                    ChatColor.translateAlternateColorCodes('&', "&7Kliknij, by odpiac.")));

            click = e -> {
                e.setCancelled(true);

                prefixUser.setSelectedPrefix(null);
                //prefixUsersDataSource.update(prefixUser);

                resetPrefixes();
                loadPrefixes();
            };

        }
        else {
            is = new ItemStack(Material.WRITABLE_BOOK);
            meta = is.getItemMeta();
            assert meta != null;
            meta.setDisplayName(ChatColor.RED + prefix.getName());
            meta.setLore(Arrays.asList("", ChatColor.translateAlternateColorCodes('&', "&eKliknij, by zalozyc prefix.")));

            click = e -> {
                e.setCancelled(true);

                prefixUser.setSelectedPrefix(prefix);
                //prefixUsersDataSource.update(prefixUser);

                resetPrefixes();
                loadPrefixes();
            };
        }

        is.setItemMeta(meta);

        gui().add(is, slot, click);

    }
}
