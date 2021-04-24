package pl.trollcraft.crv.prefix.service;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import pl.trollcraft.crv.controller.GUIController;
import pl.trollcraft.crv.prefix.controller.PrefixUsersController;
import pl.trollcraft.crv.prefix.controller.PrefixesController;
import pl.trollcraft.crv.prefix.datasource.PrefixUsersDataSource;
import pl.trollcraft.crv.prefix.model.PrefixUser;
import pl.trollcraft.crv.prefix.model.PrefixesGUI;
import pl.trollcraft.crv.prefix.model.pages.DefaultPrefixesPage;
import pl.trollcraft.crv.prefix.model.pages.MenuPage;
import pl.trollcraft.crv.prefix.model.pages.PrefixesPage;
import pl.trollcraft.crv.prefix.model.pages.PremiumPrefixesPage;
import pl.trollcraft.crv.prefix.model.prefix.PrefixType;

public class PrefixesService {

    private final GUIController guiController;
    private final PrefixesController prefixesController;
    private final PrefixUsersController prefixUsersController;
    private final PrefixUsersDataSource prefixUsersDataSource;
    private final Economy economy;

    public PrefixesService(GUIController guiController,
                           PrefixesController prefixesController,
                           PrefixUsersController prefixUsersController,
                           PrefixUsersDataSource prefixUsersDataSource,
                           Economy economy) {

        this.guiController = guiController;
        this.prefixesController = prefixesController;
        this.prefixUsersController = prefixUsersController;
        this.prefixUsersDataSource = prefixUsersDataSource;
        this.economy = economy;
    }

    public PrefixesGUI prepare(Player player) {

        PrefixUser prefixUser = prefixUsersController.find(player.getUniqueId())
                .orElseThrow(() -> new IllegalStateException("No PrefixUser for player."));

        PrefixesGUI prefixesGUI = new PrefixesGUI(player);

        MenuPage menuPage = new MenuPage(prefixesGUI);
        guiController.register(menuPage.gui());
        prefixesGUI.addPage(menuPage);
        prefixesGUI.setMainPage("menu");

        PrefixesPage defaultPrefixesPage;

        if (player.hasPermission("prefixes.premium")) {

            defaultPrefixesPage = new PremiumPrefixesPage("defaultPrefixes", PrefixType.DEFAULT, prefixesGUI,
                    prefixesController, prefixUser, prefixUsersDataSource, economy);

            PremiumPrefixesPage premiumPrefixesPage = new PremiumPrefixesPage("premiumPrefixes", PrefixType.PREMIUM, prefixesGUI,
                    prefixesController, prefixUser, prefixUsersDataSource, economy);

            guiController.register(premiumPrefixesPage.gui());
            prefixesGUI.addPage(premiumPrefixesPage);

        }
        else
            defaultPrefixesPage = new DefaultPrefixesPage("defaultPrefixes", prefixesGUI,
                    prefixesController, prefixUser, prefixUsersDataSource, economy);

        guiController.register(defaultPrefixesPage.gui());
        prefixesGUI.addPage(defaultPrefixesPage);

        return prefixesGUI;

    }

}
