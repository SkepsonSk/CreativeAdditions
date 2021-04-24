package pl.trollcraft.crv.games.service;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.trollcraft.crv.controller.GUIController;
import pl.trollcraft.crv.games.controller.PlayableController;
import pl.trollcraft.crv.games.model.Attraction;
import pl.trollcraft.crv.games.model.Playable;
import pl.trollcraft.crv.help.Help;
import pl.trollcraft.crv.model.GUI;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GameGUIService {

    private final static int MAX_PER_PAGE = 28;

    private final PlayableController playableController;
    private final GUIController guiController;

    public GameGUIService(PlayableController playableController,
                          GUIController guiController) {

        this.playableController = playableController;
        this.guiController = guiController;
    }

    public void open(Player player, int page) {

        List<Playable> playableList = playableController.findAllOpened();

        if (playableList.isEmpty()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cBrak dostepnych gier :/\nUtworz jakas!"));
            return;
        }

        Optional<GUI> oGUI = guiController.find(player);
        GUI gui;

        if (oGUI.isPresent())
            gui = oGUI.get();

        else {
            gui = new GUI(ChatColor.translateAlternateColorCodes('&', "&a&lGRY TC!"), 54, true);
            guiController.register(gui);
            guiController.register(player, gui);
        }

        ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

        gui.add(blackPane, 9);
        gui.add(blackPane, 44);
        gui.add(blackPane, 36);
        gui.add(blackPane, 17);

        for (int i = 0 ; i < 9 ; i++) {
            gui.add(blackPane, i);
            gui.add(blackPane, i+45);
        }

        ItemStack redPane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack limePane = new ItemStack(Material.LIME_STAINED_GLASS_PANE);

        // Paging panes.
        if (page == 0) {
            gui.add(redPane, 18);
            gui.add(redPane, 27);
        }
        else {
            gui.add(limePane, 18, e -> open(player, page-1));
            gui.add(limePane, 27, e -> open(player, page-1));
        }

        if ( playableList.size() > (page+1)* MAX_PER_PAGE) {
            gui.add(limePane, 26, e -> open(player, page+1));
            gui.add(limePane, 35, e -> open(player, page+1));
        }
        else {
            gui.add(redPane, 26);
            gui.add(redPane, 35);
        }

        int slot = 10;
        for (int i = page*MAX_PER_PAGE ; i < (page+1)*MAX_PER_PAGE ; i++, slot++) {

            if (i >= playableList.size())
                break;

            Playable playable = playableList.get(i);
            display(playable, gui, slot, player);

            if (slot == 16 || slot == 25 || slot == 34)
                slot += 2;

        }

        gui.open(player);

    }

    private void display(Playable playable, GUI gui, int slot, Player p) {

        ItemStack itemStack = new ItemStack(Material.WHITE_WOOL);
        ItemMeta meta = itemStack.getItemMeta();

        OfflinePlayer player = Bukkit.getOfflinePlayer(playable.author());

        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                String.format("&e%s", playable.name())));

        List<String> lore;
        if (playable instanceof Attraction) {

            Attraction attraction = (Attraction) playable;

            lore = Arrays.asList(
                    ChatColor.translateAlternateColorCodes('&', "&aTyp gry: &e&l" + playable.type().getSimpleName()),
                    ChatColor.translateAlternateColorCodes('&', "&aAutor: &e&l" + player.getName()),
                    "",
                    ChatColor.translateAlternateColorCodes('&', "&aRozegrana razy: &e&l" + attraction.getPlayedBy()),
                    ChatColor.translateAlternateColorCodes('&', "&aUkonczona razy: &e&l" + attraction.getFinishedBy()),
                    "",
                    ChatColor.translateAlternateColorCodes('&', "&aTrudnosc: &e&l" + attraction.getDifficulty()),
                    "",
                    ChatColor.translateAlternateColorCodes('&', "&eKliknij, by &e&lZAGRAC!")
            );

        }
        else
            lore = Arrays.asList(
                    ChatColor.translateAlternateColorCodes('&', "&aTyp gry: &e&l" + playable.type().getSimpleName()),
                    ChatColor.translateAlternateColorCodes('&', "&aAutor: &e&l" + player.getName()),
                    "",
                    ChatColor.translateAlternateColorCodes('&', "&eKliknij, by zagrac!")
            );

        meta.setLore(lore);

        itemStack.setItemMeta(meta);

        gui.add(itemStack, slot, e -> {

            Optional<Playable> oCurrent = playableController.findByPlaying(p);

            if (oCurrent.isPresent()) {
                Playable current = oCurrent.get();
                playableController.getService(current.type()).quit(p, current);
                Help.send(p, "&7Opuszczono obecna gre.");
            }

            p.closeInventory();

            playableController
                    .getService(playable.type())
                    .join(p, playable);

            Help.send(p, "&aDolaczono do gry!");

        });

    }

}
