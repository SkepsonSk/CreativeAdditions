package pl.trollcraft.crv.we.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.trollcraft.crv.we.controller.WorldEditUsersController;
import pl.trollcraft.crv.we.model.WorldEditUser;

import java.util.Optional;

public class WorldEditUserListener implements Listener {

    private final WorldEditUsersController worldEditUsersController;

    public WorldEditUserListener(WorldEditUsersController worldEditUsersController) {
        this.worldEditUsersController = worldEditUsersController;
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent event) {

        Player player = event.getPlayer();
        Optional<WorldEditUser> oUser = worldEditUsersController.find(player);

        if (oUser.isPresent())
            return;

        WorldEditUser user = new WorldEditUser(player);
        worldEditUsersController.register(user);

    }

    @EventHandler
    public void onQuit (PlayerQuitEvent event) {

        Player player = event.getPlayer();
        Optional<WorldEditUser> oUser = worldEditUsersController.find(player);

        if (oUser.isEmpty())
            return;

        WorldEditUser user = oUser.get();
        worldEditUsersController.unregister(user);

    }

}
