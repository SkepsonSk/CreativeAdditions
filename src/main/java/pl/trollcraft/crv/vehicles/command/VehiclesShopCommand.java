package pl.trollcraft.crv.vehicles.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.crv.vehicles.service.VehiclesService;

public class VehiclesShopCommand implements CommandExecutor {

    private final VehiclesService vehiclesService;

    public VehiclesShopCommand(VehiclesService vehiclesService) {
        this.vehiclesService = vehiclesService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;
            vehiclesService.openShop(player);

            return true;

        }

        sender.sendMessage("Komenda jedynie dla graczy online.");
        return true;
    }

}
