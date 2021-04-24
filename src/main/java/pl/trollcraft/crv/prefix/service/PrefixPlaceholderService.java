package pl.trollcraft.crv.prefix.service;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import pl.trollcraft.crv.prefix.controller.PrefixUsersController;
import pl.trollcraft.crv.prefix.model.PrefixUser;

import java.util.UUID;

public class PrefixPlaceholderService extends PlaceholderExpansion {

    private final PrefixUsersController prefixUsersController;

    public PrefixPlaceholderService(PrefixUsersController prefixUsersController) {
        this.prefixUsersController = prefixUsersController;
    }

    @Override
    public String getIdentifier() {
        return "cprefix";
    }

    @Override
    public String getAuthor() {
        return "Jakub Zelmanowicz";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {

        UUID id = p.getUniqueId();
        PrefixUser user = prefixUsersController.find(id).orElseThrow(
                () -> new IllegalStateException("No PrefixUser for " + p.getName()));

        if (user.getSelectedPrefix() != null)
            return user.getSelectedPrefix().getName();

        return "";

    }
}
