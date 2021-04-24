package pl.trollcraft.crv.we.controller;

import org.bukkit.entity.Player;
import pl.trollcraft.crv.we.model.WorldEditUser;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class WorldEditUsersController {

    private final List<WorldEditUser> users;

    public WorldEditUsersController() {
        users = new LinkedList<>();
    }

    public void register(WorldEditUser user) {
        users.add(user);
    }

    public Optional<WorldEditUser> find(Player player) {
        return users.stream()
                .filter( user -> user.getPlayer().equals(player) )
                .findFirst();
    }

    public void unregister(WorldEditUser user) {
        users.remove(user);
    }

}
