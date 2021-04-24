package pl.trollcraft.crv.prefix.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.trollcraft.crv.prefix.controller.PrefixUsersController;
import pl.trollcraft.crv.prefix.datasource.PrefixUsersDataSource;
import pl.trollcraft.crv.prefix.model.PrefixUser;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class PrefixUserListener implements Listener {

    private final PrefixUsersController prefixUsersController;
    private final PrefixUsersDataSource prefixUsersDataSource;

    public PrefixUserListener(PrefixUsersController prefixUsersController,
                              PrefixUsersDataSource prefixUsersDataSource) {

        this.prefixUsersController = prefixUsersController;
        this.prefixUsersDataSource = prefixUsersDataSource;
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent event) {

        Player player = event.getPlayer();
        UUID id = player.getUniqueId();

        if (prefixUsersController.exists(id))
            return;

        Optional<PrefixUser> oUser = prefixUsersDataSource.get(id);

        if (oUser.isPresent())
            prefixUsersController.register(oUser.get());

        else {
            PrefixUser user = new PrefixUser(id, new ArrayList<>(), null);
            prefixUsersDataSource.insert(user);
            prefixUsersController.register(user);
        }

    }

}
