package pl.trollcraft.crv.games.service;

import org.bukkit.entity.Player;
import pl.trollcraft.crv.games.model.Playable;

public interface GameService {

    boolean join(Player player, Playable t);
    void quit(Player player, Playable t);

}
