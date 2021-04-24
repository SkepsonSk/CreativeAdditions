package pl.trollcraft.crv.we.model;

import org.bukkit.entity.Player;

public class WorldEditUser {

    private final Player player;

    private long joined; // Used to define the time when the user would be able to use WE.
    private long lastUsed; // Last used WE command.

    public WorldEditUser(Player player) {
        this.player = player;
        joined = System.currentTimeMillis();
        lastUsed = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public long getJoined() {
        return joined;
    }

    public long getLastUsed() {
        return lastUsed;
    }

    public void setJoined(long joined) {
        this.joined = joined;
    }

    public void setLastUsed(long lastUsed) {
        this.lastUsed = lastUsed;
    }
}
