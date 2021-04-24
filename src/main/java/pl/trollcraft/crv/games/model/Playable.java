package pl.trollcraft.crv.games.model;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface Playable {

    UUID id();
    Class<? extends Playable> type();

    String name();
    UUID author();

    void join(Player player);
    void quit(Player player);

    boolean opened();

    boolean participates(Player player);

}
