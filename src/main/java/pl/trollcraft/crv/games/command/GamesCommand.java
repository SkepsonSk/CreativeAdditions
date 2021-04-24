package pl.trollcraft.crv.games.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.crv.games.controller.PlayableController;
import pl.trollcraft.crv.games.model.Attraction;
import pl.trollcraft.crv.games.model.Playable;
import pl.trollcraft.crv.games.service.GameGUIService;
import pl.trollcraft.crv.games.service.GameService;

import java.util.List;
import java.util.Optional;

public class GamesCommand implements CommandExecutor {

    private final PlayableController playableController;
    private final GameGUIService gameGUIService;

    public GamesCommand(PlayableController playableController,
                        GameGUIService gameGUIService) {

        this.playableController = playableController;
        this.gameGUIService = gameGUIService;
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

        if (args.length == 0) {

            gameGUIService.open(player, 0);

            /*player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eGry:"));
            player.sendMessage("");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/" + s + " losowa - dolacza do losowej gry,"));
            player.sendMessage("");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/" + s + " dolacz <nazwa> - dolacza do gry,"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/" + s + " opusc <nazwa> - opuszcza gre,"));
            player.sendMessage("");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/" + s + " info <nazwa> - informacje o grze,"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/" + s + " <gracz> - lista gier gracza."));
            player.sendMessage("");*/

            return true;

        }

        /*else if (args[0].equalsIgnoreCase("losowa")) {

            if (playableController.isPlaying(player)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cBierzesz juz udzial w innej grze."));
                return true;
            }

            Optional<Playable> oPlayable = playableController.findRandom();
            if (oPlayable.isEmpty()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cBrak dostepnych gier."));
                return true;
            }

            Playable playable = oPlayable.get();
            GameService service = playableController.getService(playable.type());
            OfflinePlayer author = Bukkit.getOfflinePlayer(playable.author());
            service.join(player, playable);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aDolaczono do gry gracza &e" + author.getName() + " (" + playable.type().getSimpleName() + ")"));

            return true;

        }*/

        else if (args[0].equalsIgnoreCase("dolacz")) {

            if (playableController.isPlaying(player)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cBierzesz juz udzial w innej grze."));
                return true;
            }

            if (args.length < 2) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7Uzycie: /" + s + " join <nazwa>"));
                return true;
            }

            StringBuilder name = new StringBuilder();
            for (int i = 1 ; i < args.length ; i++)
                name.append(args[i]).append(' ');

            Optional<Playable> oGame = playableController.findByName(name.toString().trim());

            if (oGame.isEmpty()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cNieznana gra."));
                return true;
            }

            Playable playable = oGame.get();
            GameService service = playableController.getService(playable.type());

            if (service.join(player, playable))
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&aDolaczono do gry."));

        }

        else if (args[0].equalsIgnoreCase("opusc")) {

            Optional<Attraction> oAttraction = playableController.findByParticipant(player);

            if (oAttraction.isEmpty()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cNie bierzesz udzialu w zadnej grze."));
                return true;
            }

            Attraction attraction = oAttraction.get();
            GameService service = playableController.getService(attraction.type());
            service.quit(player, attraction);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&7Opuszczono gre."));
            return true;

        }

        else if (args[0].equalsIgnoreCase("info")) {

            if (args.length < 2) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7Uzycie: /" + s + " join <nazwa>"));
                return true;
            }

            StringBuilder name = new StringBuilder();
            for (int i = 1 ; i < args.length ; i++)
                name.append(args[i]).append(' ');

            Optional<Playable> oGame = playableController.findByName(name.toString().trim());

            if (oGame.isEmpty()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cNieznana gra."));
                return true;
            }

            Playable playable = oGame.get();
            OfflinePlayer author = Bukkit.getOfflinePlayer(playable.author());

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aInformacje o grze: &e" + playable.name()));
            player.sendMessage("");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTyp: &e" + playable.type().getSimpleName()));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aAutor: &e" + author.getName()));
            player.sendMessage("");

            if (playable instanceof Attraction) {
                Attraction attraction = (Attraction) playable;
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRozegrana razy: &e" + attraction.getPlayedBy()));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aUkonczona razy: &e" + attraction.getFinishedBy()));
                player.sendMessage("");
            }

        }

        else {

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

            List<Playable> playableList = playableController.findByAuthor(offlinePlayer.getUniqueId());

            if (playableList.isEmpty()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cBrak gier od tego gracza."));
                return true;
            }

            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aGry gracza " + offlinePlayer.getName() + ":"));
            player.sendMessage("");

            playableList.forEach( playable -> {

                String color = playable.opened() ? "&a" : "&c";
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        color + playable.name() + " - &etyp: " + playable.type().getSimpleName()));

            } );
            player.sendMessage("");

            return true;

        }

        return true;
    }
}
