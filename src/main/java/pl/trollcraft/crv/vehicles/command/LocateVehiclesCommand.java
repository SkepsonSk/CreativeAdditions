package pl.trollcraft.crv.vehicles.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.crv.controller.DefaultConfigController;
import pl.trollcraft.crv.vehicles.model.AbstractVehicle;
import pl.trollcraft.crv.vehicles.service.VehiclesService;

import java.util.Collection;

public class LocateVehiclesCommand implements CommandExecutor {

    private final DefaultConfigController defaultConfigController;
    private final VehiclesService vehiclesService;

    public LocateVehiclesCommand(DefaultConfigController defaultConfigController,
                                 VehiclesService vehiclesService) {

        this.defaultConfigController = defaultConfigController;
        this.vehiclesService = vehiclesService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 0) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("Komenda jedynie dla graczy online.");
                return true;
            }

            Player player = (Player) sender;
            this.sendVehicleLocationInformation(player, player);
            return true;

        }
        else if (args.length == 1){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            this.sendVehicleLocationInformation(offlinePlayer, sender);
            return true;
        }
        else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&eUzycie: &7/" + s + " (gracz)"));
        }

        return true;
    }

    private void sendVehicleLocationInformation(OfflinePlayer player, CommandSender calling) {
        Collection<AbstractVehicle> vehicles = this.vehiclesService
                .findVehiclesForPlayer(player);

        if (vehicles.isEmpty()) {
            calling.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&7Brak pojazdow."));
        }
        else {

            if (player instanceof Player) {
                int maximumVehicles = defaultConfigController.getMaximumVehicles().get( (Player) player);
                calling.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7Pojazdy (" + vehicles.size()  + " / " + maximumVehicles + "):"));
            }
            else {
                calling.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7Pojazdy (" + vehicles.size()  + "):"));
            }

            for (AbstractVehicle vehicle : vehicles) {
                calling.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&e - " + vehicle.toString()));
            }

        }
    }

}
