package pl.trollcraft.crv;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;
import pl.trollcraft.crv.config.ConfigProvider;
import pl.trollcraft.crv.config.config.DataSourceConfig;
import pl.trollcraft.crv.controller.CredentialsController;
import pl.trollcraft.crv.controller.GUIController;
import pl.trollcraft.crv.datasource.DatabaseProvider;
import pl.trollcraft.crv.games.command.GamesCommand;
import pl.trollcraft.crv.games.command.ParkourCommand;
import pl.trollcraft.crv.games.controller.EditorsController;
import pl.trollcraft.crv.games.controller.PlayableController;
import pl.trollcraft.crv.games.listener.CommandsListener;
import pl.trollcraft.crv.games.model.parkour.Parkour;
import pl.trollcraft.crv.games.service.GameGUIService;
import pl.trollcraft.crv.games.service.ParkourService;
import pl.trollcraft.crv.help.blocksdetector.BlockDetector;
import pl.trollcraft.crv.prefix.controller.PrefixUsersController;
import pl.trollcraft.crv.prefix.datasource.PrefixUsersDataSource;
import pl.trollcraft.crv.prefix.datasource.PrefixesDataSource;
import pl.trollcraft.crv.listener.GUIListener;
import pl.trollcraft.crv.prefix.command.AdminPrefixesCommand;
import pl.trollcraft.crv.prefix.command.PrefixesCommand;
import pl.trollcraft.crv.prefix.controller.PrefixesController;
import pl.trollcraft.crv.prefix.listener.PrefixUserListener;
import pl.trollcraft.crv.prefix.service.PrefixPlaceholderService;
import pl.trollcraft.crv.prefix.service.PrefixesService;
import pl.trollcraft.crv.we.config.WorldEditConfig;
import pl.trollcraft.crv.we.controller.WorldEditUsersController;
import pl.trollcraft.crv.we.listener.WorldEditUseListener;
import pl.trollcraft.crv.we.listener.WorldEditUserListener;
import pl.trollcraft.crv.we.model.WorldEditRestriction;

import java.util.Objects;

public class CreativePlugin extends JavaPlugin {

    private ConfigProvider dataSourceProvider;

    private PrefixUsersController prefixUsersController;
    private PrefixUsersDataSource prefixUsersDataSource;

    private ParkourService parkourService;
    private PlayableController playableController;

    @Override
    public void onLoad() {
        dataSourceProvider = new ConfigProvider(this, "datasource.yml");
    }

    @Override
    public void onEnable() {

        Economy economy = Objects.requireNonNull(getServer()
                .getServicesManager()
                .getRegistration(Economy.class))
                .getProvider();

        CredentialsController credentialsController = new CredentialsController();
        new DataSourceConfig(credentialsController).configure(dataSourceProvider);

        DatabaseProvider databaseProvider = new DatabaseProvider(credentialsController);

        PrefixesDataSource prefixesDataSource = new PrefixesDataSource(databaseProvider);
        prefixesDataSource.prepare();

        PrefixesController prefixesController = new PrefixesController();
        prefixesController.register(prefixesDataSource.getAll());

        prefixUsersDataSource = new PrefixUsersDataSource(databaseProvider, prefixesController);
        prefixUsersDataSource.prepare();

        prefixUsersController = new PrefixUsersController();

        GUIController guiController = new GUIController();

        PrefixesService prefixesService = new PrefixesService(guiController, prefixesController,
                prefixUsersController, prefixUsersDataSource, economy);

        WorldEditUsersController worldEditUsersController = new WorldEditUsersController();
        WorldEditRestriction worldEditRestriction = new WorldEditRestriction();
        ConfigProvider worldEditProvider = new ConfigProvider(this, "worldedit.yml");
        WorldEditConfig worldEditConfig = new WorldEditConfig(worldEditRestriction);
        worldEditConfig.configure(worldEditProvider);

        BlockDetector blockDetector = new BlockDetector(this);
        blockDetector.monitor();

        playableController = new PlayableController();

        ConfigProvider parkourProvider = new ConfigProvider(this, "parkours.yml");
        parkourService = new ParkourService(parkourProvider, blockDetector);
        playableController.register(parkourService.load());
        playableController.register(Parkour.class, parkourService);

        GameGUIService gameGUIService = new GameGUIService(playableController, guiController);

        EditorsController editorsController = new EditorsController();

        getServer().getPluginManager().registerEvents(new GUIListener(guiController), this);

        getCommand("prefix").setExecutor(new PrefixesCommand(prefixesService));
        getCommand("prefixadmin").setExecutor(new AdminPrefixesCommand(prefixesController, prefixesDataSource, prefixUsersDataSource));

        getCommand("parkour").setExecutor(new ParkourCommand(playableController, editorsController, parkourService));
        getCommand("games").setExecutor(new GamesCommand(playableController, gameGUIService));

        getServer().getPluginManager().registerEvents(new PrefixUserListener(prefixUsersController, prefixUsersDataSource), this);

        getServer().getPluginManager().registerEvents(new WorldEditUserListener(worldEditUsersController), this);
        getServer().getPluginManager().registerEvents(new WorldEditUseListener(worldEditRestriction, worldEditUsersController), this);

        getServer().getPluginManager().registerEvents(new CommandsListener(playableController), this);

        new PrefixPlaceholderService(prefixUsersController).register();

    }

    @Override
    public void onDisable() {
        prefixUsersDataSource.updateAll(prefixUsersController.getUsers());
        parkourService.saveAll(playableController.findByType(Parkour.class));
    }

}
