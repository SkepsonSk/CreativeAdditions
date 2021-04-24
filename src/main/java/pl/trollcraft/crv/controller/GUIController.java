package pl.trollcraft.crv.controller;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pl.trollcraft.crv.model.GUI;

import java.util.*;
import java.util.stream.Collectors;

public class GUIController {

    private final List<GUI> guis;
    private final Map<Player, GUI> opened;

    public GUIController() {
        guis = new ArrayList<>();
        opened = new HashMap<>();
    }

    public void register(GUI gui) {
        guis.add(gui);
    }

    public void register(Player player, GUI gui) {
        opened.put(player, gui);
    }

    public void unregister(GUI gui) {
        guis.remove(gui);

        List<Player> toUnregister = new ArrayList<>();
        opened.forEach( ((player, g) -> {

            if (g.equals(gui))
                toUnregister.add(player);

        } ) );

        toUnregister.forEach(this::unregister);

    }

    public void unregister(Player player) {
        opened.remove(player);
    }

    public Optional<GUI> find(Inventory inventory) {
        return guis.stream()
                .filter( gui -> gui.getInventory().equals(inventory) )
                .findFirst();
    }

    public Optional<GUI> find(Player player) {
        if (!opened.containsKey(player))
            return Optional.empty();
        return Optional.of(opened.get(player));
    }

}
