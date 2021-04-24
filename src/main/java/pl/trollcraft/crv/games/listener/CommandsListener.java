package pl.trollcraft.crv.games.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.trollcraft.crv.games.controller.PlayableController;

public class CommandsListener implements Listener {

    private final PlayableController playableController;

    public CommandsListener(PlayableController playableController) {
        this.playableController = playableController;
    }

    @EventHandler
    public void onCommand (PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();

        if (!playableController.isPlaying(player))
            return;

        if (event.getMessage().startsWith("/gra") || event.getMessage().startsWith("/gry") || event.getMessage().startsWith("/game"))
            return;

        event.setCancelled(true);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&cPodczas gry nie mozesz uzywac komend poza &e/gra"));

    }

}
