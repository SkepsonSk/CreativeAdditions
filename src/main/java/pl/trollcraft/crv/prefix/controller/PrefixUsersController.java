package pl.trollcraft.crv.prefix.controller;

import pl.trollcraft.crv.prefix.model.PrefixUser;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PrefixUsersController {

    private final List<PrefixUser> users;

    public PrefixUsersController() {
        users = new LinkedList<>();
    }

    public void register(PrefixUser user) {
        users.add(user);
    }

    public boolean exists(UUID id) {
        return users.stream()
                .anyMatch( user -> user.getId().equals(id) );
    }

    public Optional<PrefixUser> find(UUID id) {
        return users.stream()
                .filter( user -> user.getId().equals(id) )
                .findFirst();
    }

    public void unregister(PrefixUser prefixUser) {
        users.remove(prefixUser);
    }

    public List<PrefixUser> getUsers() {
        return users;
    }
}
