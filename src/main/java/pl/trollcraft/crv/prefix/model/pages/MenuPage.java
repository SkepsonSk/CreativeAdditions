package pl.trollcraft.crv.prefix.model.pages;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.trollcraft.crv.prefix.model.PrefixPage;
import pl.trollcraft.crv.prefix.model.PrefixesGUI;

public final class MenuPage extends PrefixPage {

    public MenuPage(PrefixesGUI prefixesGUI) {
        super("menu", "Prefix'y", 27, prefixesGUI);
    }

    @Override
    protected void initGUI() {

        ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

        ItemStack defaultPrefixes = new ItemStack(Material.CHEST);
        ItemMeta defaultMeta = defaultPrefixes.getItemMeta();
        assert defaultMeta != null;
        defaultMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                "&7&lZwykle PREFIKSY"));
        defaultPrefixes.setItemMeta(defaultMeta);

        ItemStack premiumPrefixes = new ItemStack(Material.CHEST);
        ItemMeta premiumMeta = defaultPrefixes.getItemMeta();
        assert premiumMeta != null;
        premiumMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                "&e&lPREFIKSY PREMIUM"));
        premiumPrefixes.setItemMeta(premiumMeta);

        for (int i = 1 ; i <= 7 ; i++) {
            gui().add(blackPane, i);

            if (i + 9 == 12)
                gui().add(defaultPrefixes, 12, e -> {
                    e.setCancelled(true);
                    getPrefixesGUI().open("defaultPrefixes");
                });

            else if (i + 9 == 14)
                gui().add(premiumPrefixes, 14, e -> {
                    e.setCancelled(true);

                    if (getPrefixesGUI().getPlayer().hasPermission("prefixes.premium"))
                        getPrefixesGUI().open("premiumPrefixes");
                    else
                        getPrefixesGUI().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cPrefix'y sa jedynie dla graczy MVIP!"));

                });

            else
                gui().add(blackPane, i+9);

            gui().add(blackPane, i+18);
        }

        gui().add(blackPane, 9);
        gui().add(blackPane, 17);

        ItemStack yellowPane = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        gui().add(yellowPane, 0);
        gui().add(yellowPane, 8);
        gui().add(yellowPane, 18);
        gui().add(yellowPane, 26);

    }

}
