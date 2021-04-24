package pl.trollcraft.crv.games.controller;

import org.bukkit.entity.Player;
import pl.trollcraft.crv.games.model.Editor;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class EditorsController {

    private final List<Editor> editors;

    public EditorsController() {
        editors = new LinkedList<>();
    }

    public void register(Editor editor) {
        editors.add(editor);
    }

    public void unregister(Editor editor) {
        editors.remove(editor);
    }

    public Optional<Editor> find(Player player) {
        return editors.stream()
                .filter(editor -> editor.getPlayer().equals(player))
                .findFirst();
    }

}
