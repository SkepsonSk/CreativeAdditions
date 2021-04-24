package pl.trollcraft.crv.games.model.parkour;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.trollcraft.crv.games.model.Attraction;
import pl.trollcraft.crv.games.model.Playable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Parkour extends Attraction {

    private Location start;

    private Material checkpoint;
    private Material kill;
    private Material finish;

    private final Map<Player, Location> checkpoints;

    public Parkour(UUID author, String name) {
        super(author, name);
        checkpoints = new HashMap<>();
    }

    public Parkour(UUID id,
                   String name,
                   UUID author,
                   int playedBy,
                   int finishedBy,
                   boolean open,
                   Location start,
                   Material kill,
                   Material checkpoint,
                   Material finish) {

        super(id, name, author, playedBy, finishedBy, open);
        checkpoints = new HashMap<>();

        this.start = start;
        this.kill = kill;
        this.checkpoint = checkpoint;
        this.finish = finish;
    }

    @Override
    public Class<? extends Playable> type() {
        return Parkour.class;
    }

    @Override
    public void join(Player player) {
        super.join(player);
        checkpoints.put(player, start);
        player.teleport(start);
        player.setGameMode(GameMode.ADVENTURE);
    }

    @Override
    public void quit(Player player) {
        super.quit(player);
        checkpoints.remove(player);
        player.setGameMode(GameMode.CREATIVE);
    }

    @Override
    public void finish(Player player) {
        addFinished();
    }

    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Material getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(Material checkpoint) {
        this.checkpoint = checkpoint;
    }

    public Material getKill() {
        return kill;
    }

    public void setKill(Material kill) {
        this.kill = kill;
    }

    public Material getFinish() {
        return finish;
    }

    public void setFinish(Material finish) {
        this.finish = finish;
    }

    public Location getCheckpoint(Player player) {
        return checkpoints.get(player);
    }

    public void checkpoint(Player player) {
        checkpoints.replace(player, player.getLocation());
    }

}
