package pl.trollcraft.crv.vehicles.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.trollcraft.crv.help.Help;
import pl.trollcraft.crv.vehicles.service.VehiclesService;

import javax.annotation.Nonnull;

public class RemoveVehiclesCommand implements CommandExecutor {

    private final VehiclesService vehiclesService;

    public RemoveVehiclesCommand(VehiclesService vehiclesService) {
        this.vehiclesService = vehiclesService;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender,
                             @Nonnull Command command,
                             @Nonnull String s,
                             @Nonnull String[] args) {

        if (!sender.hasPermission("creative.vehicles.manage")) {
            Help.send(sender, "&cBrak uprawnien.");
            return true;
        }

        if (args.length == 0) {
            Help.send(sender, "&7Uzycie: &e/creative-vehicles-remove <all|(gracz)>");
            return true;
        }

        if (args[0].equalsIgnoreCase("all")){
            vehiclesService.removeAll();
            Help.send(sender, "&7Usunieto wszystkie pojazdy.");
        }
        else {

            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
            vehiclesService.remove(player);

            if (player.isOnline() && player.getPlayer() != null) {
                Help.send(player.getPlayer(), "&7Administrator usunal wszystkie Twoje pojazdy.");
            }

            Help.send(sender, "&7Usunieto pojazdy gracza.");

        }

        return true;
    }
}
