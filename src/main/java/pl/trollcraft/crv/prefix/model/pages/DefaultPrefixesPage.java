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

public final class DefaultPrefixesPage extends PrefixesPage {

    public DefaultPrefixesPage(String name,
                               PrefixesGUI prefixesGUI,
                               PrefixesController prefixesController,
                               PrefixUser prefixUser,
                               PrefixUsersDataSource prefixUsersDataSource,
                               Economy economy) {

        super(name, PrefixType.DEFAULT, prefixesGUI, prefixesController, prefixUser, prefixUsersDataSource, economy);
    }

    @Override
    protected void displayPrefix(Prefix prefix, int slot) {

        ItemStack is;
        ItemMeta meta;
        Consumer<InventoryClickEvent> click;

        if (prefixUser.hasPrefix(prefix)){

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

        }
        else {
            is = new ItemStack(Material.BOOK);
            meta = is.getItemMeta();
            assert meta != null;
            meta.setDisplayName(ChatColor.RED + prefix.getName());
            meta.setLore(Arrays.asList("", ChatColor.translateAlternateColorCodes('&', "&7Cena: &e" + prefix.getPrice() + "TC"),
                    "", ChatColor.translateAlternateColorCodes('&', "&eKliknij, by odblokowac.")));

            click = e -> {

                e.setCancelled(true);

                if (!economy.has(offlinePlayer, prefix.getPrice()))
                    e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cBrak srodkow."));

                else {

                    economy.withdrawPlayer(offlinePlayer, prefix.getPrice());
                    prefixUser.getPrefixes().add(prefix);

                    prefixUsersDataSource.addPrefix(prefixUser, prefix);

                    resetPrefixes();
                    loadPrefixes();
                }


            };
        }

        is.setItemMeta(meta);

        gui().add(is, slot, click);

    }
}
