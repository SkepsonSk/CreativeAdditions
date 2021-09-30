package pl.trollcraft.crv.vehicles.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.trollcraft.crv.config.ConfigProvider;
import pl.trollcraft.crv.controller.GUIController;
import pl.trollcraft.crv.help.Help;
import pl.trollcraft.crv.model.GUI;
import pl.trollcraft.crv.vehicles.controller.VehiclesController;
import pl.trollcraft.crv.vehicles.model.AbstractVehicle;
import pl.trollcraft.crv.vehicles.model.ObtainableVehicle;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class VehiclesService {

    private final List<AbstractVehicle> vehicles;
    private final Multimap<OfflinePlayer, AbstractVehicle> playerSpawnedVehicles;
    private final ConfigProvider vehiclesProvider;
    private final Economy economy;
    private final VehiclesController vehiclesController;
    private final GUIController guiController;

    public VehiclesService(ConfigProvider vehiclesProvider,
                           Economy economy,
                           VehiclesController vehiclesController,
                           GUIController guiController) {

        vehicles = new LinkedList<>();
        playerSpawnedVehicles = ArrayListMultimap.create();
        this.vehiclesProvider = vehiclesProvider;
        this.economy = economy;
        this.vehiclesController = vehiclesController;
        this.guiController = guiController;
    }

    public void sell(Player player, ObtainableVehicle vehicle) {

        if (economy.has(player, vehicle.getPrice())) {
            economy.withdrawPlayer(player, vehicle.getPrice());
            vehicle.give(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aZakupiono pojazd."));
        }
        else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cBrak srodkow na zakup pojazdu."));
        }

    }

    public Collection<AbstractVehicle> findVehiclesForPlayer(OfflinePlayer player) {
        return this.playerSpawnedVehicles.get(player);
    }

    public int getVehiclesAmountForPlayer(OfflinePlayer player) {
        return this.playerSpawnedVehicles.get(player).size();
    }

    public Optional<AbstractVehicle> findVehicleForPlayer(UUID vehicleUUID) {
        return this.vehicles.stream()
                .filter( vehicle -> vehicle.getVehicleUUID().equals(vehicleUUID) )
                .findFirst();
    }

    public void openShop(Player player) {
        guiController.unregister(player);
        GUI gui = new GUI("&eSklep z pojazdami", 54, true);

        vehiclesController.getVehicleCategories().forEach( vehicleCategory -> {

            ItemStack itemStack = new ItemStack(Material.CHEST);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(vehicleCategory.getDisplay());
            itemStack.setItemMeta(meta);

            gui.add(itemStack, vehicleCategory.getSlot(), e -> {
                e.setCancelled(true);
                openShopFor(player, vehicleCategory.getId());
            });

        } );

        guiController.register(gui);
        gui.open(player);
    }

    public void openShopFor(Player player, String vehicleCategory) {
        guiController.unregister(player);

        GUI gui = new GUI(vehicleCategory, 54, true);

        for (int i = 45 ; i < 54 ; i++) {

            ItemStack itemStack;
            if (i == 49) {
                itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                ItemMeta meta = itemStack.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                        "&cWroc do KATEGORII"));
                itemStack.setItemMeta(meta);
                gui.add(itemStack, i, e -> openShop(player));
            }
            else {
                itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                gui.add(itemStack, i, e -> e.setCancelled(true));
            }

        }

        vehiclesController.findByCategory(vehicleCategory).forEach(vehicle -> {

            ItemStack itemStack = new ItemStack(Material.CHEST);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(vehicle.getDisplay());
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.translateAlternateColorCodes('&',
                    "&aCena: &e" + vehicle.getPrice() + "TC"));
            meta.setLore(lore);
            itemStack.setItemMeta(meta);

            gui.add(itemStack, vehicle.getSlot(), e -> {
                e.setCancelled(true);
                sell(player, vehicle);
            });
        });

        guiController.register(gui);
        gui.open(player);
    }

    public void register(AbstractVehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void unregister(AbstractVehicle vehicle) {
        vehicles.remove(vehicle);
    }

    public void track(Player player, AbstractVehicle vehicle) {
        playerSpawnedVehicles.put(player, vehicle);
    }

    public void track(Player player) {
        List<AbstractVehicle> vehicles = this.vehicles.stream()
                .filter(abstractVehicle -> abstractVehicle.getPlayerName().equals(player.getName()))
                .collect(Collectors.toList());

        this.playerSpawnedVehicles.putAll(player, vehicles);
    }

    public void unTrack(Player player, AbstractVehicle vehicle) {
        playerSpawnedVehicles.remove(player, vehicle);
    }

    public void unTrack(Player player) {
        playerSpawnedVehicles.removeAll(player);
    }

    public void remove(OfflinePlayer player) {
        playerSpawnedVehicles.get(player).forEach(vehicle ->
            vehicle.getParts().forEach(Entity::remove)
        );

        playerSpawnedVehicles.removeAll(player);
    }

    public void removeAll() {
        vehicles.forEach(vehicle ->
            vehicle.getParts().forEach(Entity::remove)
        );

        vehicles.clear();
        playerSpawnedVehicles.clear();
    }

    public void save() {
        vehiclesProvider.write("vehicles", null);

        this.vehicles.forEach( abstractVehicle -> {

            String playerName = abstractVehicle.getPlayerName();
            vehiclesProvider.write(String.format("vehicles.%s.%s", playerName,
                            abstractVehicle.getVehicleUUID().toString()),
                    Help.toStringList(abstractVehicle));
        } );

        vehiclesProvider.save();
    }

}
