package pl.trollcraft.crv.prefix.model;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrefixesGUI {

    private final Player player;

    private String mainPage;
    private final List<PrefixPage> pages;

    public PrefixesGUI (Player player) {
        this.player = player;
        pages = new ArrayList<>();
    }

    public void setMainPage(String mainPage) {
        this.mainPage = mainPage;
    }

    public void addPage(PrefixPage page) {
        pages.add(page);
    }

    public Optional<PrefixPage> findPage(String pageName) {
        return pages.stream()
                .filter( page -> page.getName().equals(pageName) )
                .findFirst();
    }

    public void open(String pageName) {

        Optional<PrefixPage> oPage = findPage(pageName);
        if (oPage.isEmpty())
            throw new IllegalStateException("Mage does not exist.");

        PrefixPage page = oPage.get();

        if (!page.isInitialized())
            page.initialize();

        page.open(player);

    }

    public void open() {

        Optional<PrefixPage> oPage = findPage(mainPage);
        if (oPage.isEmpty())
            throw new IllegalStateException("Main page does not exist.");

        PrefixPage page = oPage.get();

        if (!page.isInitialized())
            page.initialize();

        page.open(player);

    }

    public Player getPlayer() {
        return player;
    }
}
