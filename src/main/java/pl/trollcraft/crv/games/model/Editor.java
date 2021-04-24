package pl.trollcraft.crv.games.model;

import org.bukkit.entity.Player;

public class Editor {

    private final Player player;

    private Playable playable;
    private Class<? extends Playable> type;

    public Editor(Player player,
                  Playable playable,
                  Class<? extends Playable> type) {

        this.player = player;
        this.playable = playable;
        this.type = type;
    }

    public Player getPlayer() {
        return player;
    }

    public Playable getPlayable() {
        return playable;
    }

    public Class<? extends Playable> getType() {
        return type;
    }

    public void setPlayable(Playable playable) {
        this.playable = playable;
    }

    public void setType(Class<? extends Playable> type) {
        this.type = type;
    }
}
