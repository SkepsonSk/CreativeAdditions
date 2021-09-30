package pl.trollcraft.crv;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;
import pl.trollcraft.crv.config.ConfigProvider;
import pl.trollcraft.crv.config.config.*;
import pl.trollcraft.crv.controller.CredentialsController;
import pl.trollcraft.crv.controller.DefaultConfigController;
import pl.trollcraft.crv.controller.GUIController;
import pl.trollcraft.crv.datasource.DatabaseProvider;
import pl.trollcraft.crv.entityClear.EntityClearCommand;
import pl.trollcraft.crv.entityClear.EntityClearController;
import pl.trollcraft.crv.entityClear.EntityClearService;
import pl.trollcraft.crv.games.command.GamesCommand;
import pl.trollcraft.crv.games.command.ParkourCommand;
import pl.trollcraft.crv.games.controller.EditorsController;
import pl.trollcraft.crv.games.controller.PlayableController;
import pl.trollcraft.crv.games.listener.CommandsListener;
import pl.trollcraft.crv.games.model.parkour.Parkour;
import pl.trollcraft.crv.games.service.GameGUIService;
import pl.trollcraft.crv.games.service.ParkourService;
import pl.trollcraft.crv.help.blocksdetector.BlockDetector;
import pl.trollcraft.crv.inventoryClear.ClearCommand;
import pl.trollcraft.crv.inventoryClear.clearRules.VehicleClearRule;
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
import pl.trollcraft.crv.vehicles.command.LocateVehiclesCommand;
import pl.trollcraft.crv.vehicles.command.RemoveVehiclesCommand;
import pl.trollcraft.crv.vehicles.command.VehiclesShopCommand;
import pl.trollcraft.crv.vehicles.controller.VehiclesController;
import pl.trollcraft.crv.vehicles.listener.VehiclesListener;
import pl.trollcraft.crv.vehicles.service.VehiclesService;
import pl.trollcraft.crv.vehicles.service.restriction.AmountVehicleRestriction;
import pl.trollcraft.crv.vehicles.service.restriction.VehiclesRestrictionService;
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

    private ConfigProvider vehiclesProvider;
    private VehiclesService vehiclesService;

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

        ConfigProvider defaultConfigProvider = new ConfigProvider(this, "config.yml");
        DefaultConfigController defaultConfigController = new DefaultConfigController();
        new DefaultConfig(defaultConfigController).configure(defaultConfigProvider);

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

        // ---- Vehicles ----

        ConfigProvider obtainableVehiclesProvider = new ConfigProvider(this, "obtainableVehicles.yml");
        VehiclesController vehiclesController = new VehiclesController();
        new ObtainableVehiclesConfig(vehiclesController).configure(obtainableVehiclesProvider);

        VehiclesRestrictionService vehiclesRestrictionService = new VehiclesRestrictionService();

        vehiclesProvider = new ConfigProvider(this, "vehicles.yml");
        vehiclesService = new VehiclesService(vehiclesProvider, economy, vehiclesController, guiController);
        new VehiclesConfig(vehiclesService).configure(vehiclesProvider);

        getServer().getPluginManager()
                .registerEvents(new VehiclesListener(vehiclesRestrictionService, vehiclesService), this);

        vehiclesRestrictionService.register(new AmountVehicleRestriction(defaultConfigController, vehiclesService));

        // ---- Entities Clearing ----

        EntityClearController entityClearController = new EntityClearController();
        ConfigProvider entityClearConfigProvider = new ConfigProvider(this, "entityClearing.yml");
        new EntityClearConfig(entityClearController).configure(entityClearConfigProvider);
        EntityClearService entityClearService = new EntityClearService(this, entityClearController);

        // ----

        getServer().getPluginManager().registerEvents(new GUIListener(guiController), this);

        getCommand("prefix").setExecutor(new PrefixesCommand(prefixesService));
        getCommand("prefixadmin").setExecutor(new AdminPrefixesCommand(prefixesController, prefixesDataSource, prefixUsersDataSource));

        getCommand("parkour").setExecutor(new ParkourCommand(playableController, editorsController, parkourService));
        getCommand("games").setExecutor(new GamesCommand(playableController, gameGUIService));

        getCommand("creative-vehicles-remove").setExecutor(new RemoveVehiclesCommand(vehiclesService));
        getCommand("vehicles").setExecutor(new VehiclesShopCommand(vehiclesService));

        getCommand("gdziePojazd").setExecutor(new LocateVehiclesCommand(defaultConfigController, vehiclesService));

        getCommand("clearEntities").setExecutor(new EntityClearCommand(entityClearService));

        ClearCommand clearCommand = new ClearCommand();
        clearCommand.registerRule(new VehicleClearRule());
        getCommand("clear").setExecutor(clearCommand);

        getServer().getPluginManager().registerEvents(new PrefixUserListener(prefixUsersController, prefixUsersDataSource),
                this);

        getServer().getPluginManager().registerEvents(new WorldEditUserListener(worldEditUsersController), this);
        getServer().getPluginManager().registerEvents(new WorldEditUseListener(worldEditRestriction, worldEditUsersController),
                this);

        getServer().getPluginManager().registerEvents(new CommandsListener(playableController), this);

        new PrefixPlaceholderService(prefixUsersController).register();

    }

    @Override
    public void onDisable() {
        prefixUsersDataSource.updateAll(prefixUsersController.getUsers());
        parkourService.saveAll(playableController.findByType(Parkour.class));
        vehiclesService.save();
    }

}
