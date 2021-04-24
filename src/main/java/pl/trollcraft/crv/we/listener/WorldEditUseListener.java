package pl.trollcraft.crv.we.listener;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.trollcraft.crv.we.controller.WorldEditUsersController;
import pl.trollcraft.crv.we.model.WorldEditRestriction;
import pl.trollcraft.crv.we.model.WorldEditUser;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class WorldEditUseListener implements Listener {

    private final WorldEditRestriction worldEditRestriction;
    private final WorldEditUsersController worldEditUsersController;

    public WorldEditUseListener(WorldEditRestriction worldEditRestriction,
                                WorldEditUsersController worldEditUsersController) {

        this.worldEditRestriction = worldEditRestriction;
        this.worldEditUsersController = worldEditUsersController;
    }

    @EventHandler
    public void onCommand (PlayerCommandPreprocessEvent event) {

        String command = event.getMessage();

        Optional<String> oCommand = worldEditRestriction.getLimitedCommand(command);

        if (oCommand.isPresent()) {

            Player player = event.getPlayer();

            if (player.hasPermission("creative.we.bypass"))
                return;

            WorldEditUser worldEditUser = worldEditUsersController.find(player)
                    .orElseThrow( () -> new IllegalStateException("WorldEditUser not exists.") );

            // Checking if player must wait to be able to use WE.

            if (!player.hasPermission("creative.worldedit")) {
                int start = getStart(player, worldEditRestriction); // in minutes.
                if (start > 0) {
                    // Player must wait before be able to use WE.
                    long joined = worldEditUser.getJoined();

                    if (System.currentTimeMillis() - joined < start*60L*1000L) {

                        long mins = TimeUnit.MILLISECONDS.toMinutes(start*60L*1000L - (System.currentTimeMillis() - joined));

                        event.setCancelled(true);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cMusisz grac jeszcze przez &e" + mins + " minut,&c by moc korzystac w World Edit'a"));

                        return;
                    }

                }
            }

            // Checking if player is on cooldown.

            if (worldEditUser.getLastUsed() > 0) {

                int cooldown = getCooldown(player, worldEditRestriction);

                if (System.currentTimeMillis() - worldEditUser.getLastUsed() < cooldown*1000L) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cMusisz odczekac &e" + cooldown + "s. pomiedzy uzyciami World Edit'a."));
                    return;
                }

            }

            // Preparing command.

            String weCommand = oCommand.get();
            command = command.replace(weCommand, "")
                    .replaceAll("([0-9]+)%", "").trim(); // Command without quantities ex. 43%2 - 2, 2,5,4,2,sand etc...

            // Looking for forbidden materials

            if (worldEditRestriction.containsForbidden(command)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cPolecenie zawiera bloki zablokowane."));
                return;
            }

            LocalSession session = WorldEdit.getInstance()
                    .getSessionManager()
                    .findByName(player.getName());

            if (session == null)
                return;

            try {

                Region region = session.getSelection(BukkitAdapter.adapt(player.getWorld()));
                long vol = region.getVolume();
                long maxVol = getMaxVolume(player, worldEditRestriction);

                if (vol > maxVol*maxVol*maxVol) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cZaznaczony region jest zbyt duzy.\n" +
                                    "&7Max. region dla Ciebie to: " + maxVol + "x" + maxVol));
                }

            } catch (IncompleteRegionException e) {
                e.printStackTrace();
            }

            worldEditUser.setLastUsed(System.currentTimeMillis());

        }

    }

    private long getMaxVolume(Player player, WorldEditRestriction restriction) {
        if (player.hasPermission("creative.mvip"))
            return restriction.getMaxMVip();
        else if (player.hasPermission("creative.svip"))
            return restriction.getMaxSVip();
        else if (player.hasPermission("creative.vip"))
            return restriction.getMaxVip();
        else return restriction.getMaxDefault();

    }

    private int getCooldown(Player player, WorldEditRestriction restriction) {
        if (player.hasPermission("creative.mvip"))
            return restriction.getCooldownMVip();
        else if (player.hasPermission("creative.svip"))
            return restriction.getCooldownSVip();
        else if (player.hasPermission("creative.vip"))
            return restriction.getCooldownVip();
        else return restriction.getCooldownDefault();
    }

    private int getStart(Player player, WorldEditRestriction worldEditRestriction) {
        if (player.hasPermission("creative.svip") || player.hasPermission("creative.vip"))
            return 0;

        return worldEditRestriction.getStart();
    }

}
