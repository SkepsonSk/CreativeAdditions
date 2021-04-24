package pl.trollcraft.crv.prefix.model;

import org.bukkit.entity.Player;
import pl.trollcraft.crv.model.GUI;

public abstract class PrefixPage {

    private final String name;
    private final GUI gui;
    private boolean initialized;

    private final PrefixesGUI prefixesGUI;

    public PrefixPage(String name, String title, int slots, PrefixesGUI prefixesGUI) {
        this.name = name;
        gui = new GUI(title, slots, true);
        initialized = false;
        this.prefixesGUI = prefixesGUI;
    }

    public final String getName() {
        return name;
    }

    protected abstract void initGUI();

    public PrefixesGUI getPrefixesGUI() {
        return prefixesGUI;
    }

    public GUI gui() {
        return gui;
    }

    public final void initialize() {
        initGUI();
        initialized = true;
    }

    public final boolean isInitialized() {
        return initialized;
    }

    public final void open(Player player) {
        gui.open(player);
    }

}
