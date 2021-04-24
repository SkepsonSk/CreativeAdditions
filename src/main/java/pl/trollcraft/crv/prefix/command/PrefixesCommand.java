package pl.trollcraft.crv.prefix.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.crv.prefix.service.PrefixesService;

public class PrefixesCommand implements CommandExecutor {

    private final PrefixesService prefixesService;

    public PrefixesCommand(PrefixesService prefixesService) {
        this.prefixesService = prefixesService;
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String s,
                             String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Komenda jedynie dla graczy online.");
            return true;
        }

        Player player = (Player) sender;
        prefixesService.prepare(player).open();

        return true;
    }
}
