package pl.trollcraft.crv.entityClear;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EntityClearCommand implements CommandExecutor {

    private final EntityClearService entityClearService;

    public EntityClearCommand(EntityClearService entityClearService) {
        this.entityClearService = entityClearService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!sender.hasPermission("creative.admin")) {
            sender.sendMessage("Brak uprawnien.");
            return true;
        }

        int clearingResult = entityClearService.clearEntities();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7Usunieto " + clearingResult + " stworzen."));

        return true;
    }
}
