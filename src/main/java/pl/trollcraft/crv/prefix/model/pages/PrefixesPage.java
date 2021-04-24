package pl.trollcraft.crv.prefix.model.pages;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.trollcraft.crv.prefix.controller.PrefixesController;
import pl.trollcraft.crv.prefix.datasource.PrefixUsersDataSource;
import pl.trollcraft.crv.prefix.model.PrefixPage;
import pl.trollcraft.crv.prefix.model.PrefixUser;
import pl.trollcraft.crv.prefix.model.PrefixesGUI;
import pl.trollcraft.crv.prefix.model.prefix.Prefix;
import pl.trollcraft.crv.prefix.model.prefix.PrefixType;

import java.util.List;
import java.util.function.Consumer;

public abstract class PrefixesPage extends PrefixPage {

    private static final int MAX_PREFIXES = 28;

    private final PrefixType type;
    private int page;

    protected final PrefixesController prefixesController;
    protected final PrefixUsersDataSource prefixUsersDataSource;

    protected final PrefixUser prefixUser;
    protected final OfflinePlayer offlinePlayer;
    protected final Economy economy;

    private final Consumer<InventoryClickEvent> previousPage = e -> {
        e.setCancelled(true);
        page--;
        resetPrefixes();
        loadPrefixes();
    };

    private final Consumer<InventoryClickEvent> nextPage = e -> {
        e.setCancelled(true);
        page++;
        resetPrefixes();
        loadPrefixes();
    };

    public PrefixesPage(String name,
                        PrefixType type,
                        PrefixesGUI prefixesGUI,
                        PrefixesController prefixesController,
                        PrefixUser prefixUser,
                        PrefixUsersDataSource prefixUsersDataSource,
                        Economy economy) {

        super(name, "Prefix'y", 54, prefixesGUI);
        this.type = type;
        page = 0;

        this.prefixesController = prefixesController;
        this.prefixUsersDataSource = prefixUsersDataSource;

        this.prefixUser = prefixUser;
        offlinePlayer = Bukkit.getOfflinePlayer(prefixUser.getId());

        this.economy = economy;
    }

    @Override
    protected void initGUI() {

        ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

        // Decorative panes.
        for (int i = 0 ; i < 9 ; i++) {
            gui().add(blackPane, i);
            gui().add(blackPane, i+45);
        }

        gui().add(blackPane, 9);
        gui().add(blackPane, 44);
        gui().add(blackPane, 36);
        gui().add(blackPane, 17);

        loadPrefixes();
    }

    protected void loadPrefixes() {

        List<Prefix> prefixes = prefixesController.getPrefixes(type);

        ItemStack redPane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack limePane = new ItemStack(Material.LIME_STAINED_GLASS_PANE);

        // Paging panes.
        if (page == 0) {
            gui().add(redPane, 18);
            gui().add(redPane, 27);
        }
        else {
            gui().add(limePane, 18, previousPage);
            gui().add(limePane, 27, previousPage);
        }

        if ( prefixes.size() > (page+1)* MAX_PREFIXES) {
            gui().add(limePane, 26, nextPage);
            gui().add(limePane, 35, nextPage);
        }
        else {
            gui().add(redPane, 26);
            gui().add(redPane, 35);
        }

        int slot = 10;
        for (int i = page*MAX_PREFIXES ; i < (page+1)*MAX_PREFIXES ; i++, slot++) {

            if (i >= prefixes.size())
                break;

            Prefix prefix = prefixes.get(i);
            displayPrefix(prefix, slot);

            if (slot == 16 || slot == 25 || slot == 34)
                slot += 2;

        }


    }

    protected abstract void displayPrefix(Prefix prefix, int slot);

    protected void resetPrefixes() {

        int slot = 10;
        for (int i = 0 ; i < MAX_PREFIXES ; i++, slot++) {

            gui().reset(slot);

            if (slot == 16 || slot == 25 || slot == 34)
                slot += 2;

        }

    }

}
