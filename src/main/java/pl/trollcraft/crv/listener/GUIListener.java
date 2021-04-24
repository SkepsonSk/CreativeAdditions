package pl.trollcraft.crv.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import pl.trollcraft.crv.controller.GUIController;
import pl.trollcraft.crv.model.GUI;

import java.util.Optional;

public class GUIListener implements Listener {

    private final GUIController guiController;

    public GUIListener(GUIController guiController) {
        this.guiController = guiController;
    }

    @EventHandler
    public void onClick (InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        Optional<GUI> oGUI = guiController.find(inventory);

        if (oGUI.isEmpty())
            return;

        int slot = event.getSlot();
        GUI gui = oGUI.get();

        gui.click(slot, event);
    }

    @EventHandler
    public void onClose (InventoryCloseEvent event) {

        Inventory inventory = event.getInventory();
        Optional<GUI> oGUI = guiController.find(inventory);

        if (oGUI.isEmpty())
            return;

        GUI gui = oGUI.get();
        if (gui.isTransiting())
            return;

        Player player = (Player) event.getPlayer();

        guiController.unregister(player);
        guiController.unregister(gui);

    }

}
