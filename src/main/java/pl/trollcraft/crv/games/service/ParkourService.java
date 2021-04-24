package pl.trollcraft.crv.games.service;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.trollcraft.crv.config.ConfigProvider;
import pl.trollcraft.crv.games.controller.PlayableController;
import pl.trollcraft.crv.games.model.Persistent;
import pl.trollcraft.crv.games.model.Playable;
import pl.trollcraft.crv.games.model.parkour.Parkour;
import pl.trollcraft.crv.help.Help;
import pl.trollcraft.crv.help.blocksdetector.BlockDetector;
import pl.trollcraft.crv.help.blocksdetector.DetectionRequest;

import java.util.*;

public class ParkourService implements GameService, Persistent {

    private final ConfigProvider provider;

    private final BlockDetector blockDetector;
    //private final Map<Player, Location> lastLocation;

    public ParkourService(ConfigProvider provider,
                          BlockDetector blockDetector) {

        this.provider = provider;
        this.blockDetector = blockDetector;
        //lastLocation = new HashMap<>();
    }

    @Override
    public boolean join(Player player, Playable playable) {

        Parkour parkour = (Parkour) playable;

        if (!parkour.opened()) {
            Help.send(player, "&cGra jest zamknieta.");
            return false;
        }

        DetectionRequest checkpoint = new DetectionRequest("PARKOUR", player, parkour.getCheckpoint(), req -> {

            parkour.checkpoint(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aOsiagnieto &a&lCHECKPOINT!"));

        }, true );

        DetectionRequest kill = new DetectionRequest("PARKOUR", player, parkour.getKill(), req -> {

            Location loc = parkour.getCheckpoint(player);
            player.teleport(loc);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cSpadles! Powrot do checkpoint'u."));

        }, true );

        DetectionRequest finish = new DetectionRequest("PARKOUR", player, parkour.getFinish(), req -> {

            parkour.finish(player);
            quit(player, parkour);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&e&lGratulacje! &aUkonczyles ten parkour."));

        }, true );

        blockDetector.define(checkpoint);
        blockDetector.define(kill);
        blockDetector.define(finish);

        //lastLocation.put(player, player.getLocation());
        parkour.join(player);

        return true;

    }

    @Override
    public void quit(Player player, Playable playable) {
        blockDetector.undefine(player, "PARKOUR");
        playable.quit(player);

        //Location loc = lastLocation.get(player);
        //player.teleport(loc);

        //lastLocation.remove(player);
    }

    @Override
    public void save(Playable t) {
        Parkour parkour = (Parkour) t;

        provider.write(String.format("parkours.%s.name", parkour.id().toString()), parkour.name());
        provider.write(String.format("parkours.%s.author", parkour.id().toString()), parkour.author().toString());

        provider.write(String.format("parkours.%s.playedBy", parkour.id().toString()), parkour.getPlayedBy());
        provider.write(String.format("parkours.%s.finishedBy", parkour.id().toString()), parkour.getFinishedBy());

        provider.write(String.format("parkours.%s.start", parkour.id().toString()), Help.locationToString(parkour.getStart()));

        provider.write(String.format("parkours.%s.kill", parkour.id().toString()), parkour.getKill().name());
        provider.write(String.format("parkours.%s.checkpoint", parkour.id().toString()), parkour.getCheckpoint().name());
        provider.write(String.format("parkours.%s.finish", parkour.id().toString()), parkour.getFinish().name());

        provider.write(String.format("parkours.%s.opened", parkour.id().toString()), parkour.opened());

        provider.save();
    }

    @Override
    public void saveAll(List<Playable> playableList) {

        playableList.forEach( playable -> {

            Parkour parkour = (Parkour) playable;

            provider.write(String.format("parkours.%s.name", parkour.id().toString()), parkour.name());
            provider.write(String.format("parkours.%s.author", parkour.id().toString()), parkour.author().toString());

            provider.write(String.format("parkours.%s.playedBy", parkour.id().toString()), parkour.getPlayedBy());
            provider.write(String.format("parkours.%s.finishedBy", parkour.id().toString()), parkour.getFinishedBy());

            provider.write(String.format("parkours.%s.start", parkour.id().toString()), Help.locationToString(parkour.getStart()));

            provider.write(String.format("parkours.%s.kill", parkour.id().toString()), parkour.getKill().name());
            provider.write(String.format("parkours.%s.checkpoint", parkour.id().toString()), parkour.getCheckpoint().name());
            provider.write(String.format("parkours.%s.finish", parkour.id().toString()), parkour.getFinish().name());

            provider.write(String.format("parkours.%s.opened", parkour.id().toString()), parkour.opened());

        } );

        provider.save();

    }

    @Override
    public List<Playable> load() {

        final ArrayList<Playable> playableList = new ArrayList<>();

        Objects.requireNonNull(provider.conf().getConfigurationSection("parkours"))
                .getKeys(false).forEach(idStr -> {

            String name = provider.read(String.format("parkours.%s.name", idStr), String.class);
            UUID author = UUID.fromString(provider.read(String.format("parkours.%s.author", idStr), String.class));

            int playedBy = provider.read(String.format("parkours.%s.playedBy", idStr), Integer.class);
            int finishedBy = provider.read(String.format("parkours.%s.finishedBy", idStr), Integer.class);

            Location start = Help.locationFromString(provider.read(String.format("parkours.%s.start", idStr), String.class));

            Material kill = Material.valueOf(provider.read(String.format("parkours.%s.kill", idStr), String.class));
            Material checkpoint = Material.valueOf(provider.read(String.format("parkours.%s.checkpoint", idStr), String.class));
            Material finish = Material.valueOf(provider.read(String.format("parkours.%s.finish", idStr), String.class));

            boolean open = provider.read(String.format("parkours.%s.opened", idStr), Boolean.class);

            Parkour parkour = new Parkour(UUID.fromString(idStr), name, author,
                    playedBy, finishedBy, open, start, kill, checkpoint, finish);

            playableList.add(parkour);

        });

        return playableList;
    }

    @Override
    public void delete(Playable p) {
        provider.write("parkours." + p.id(), null);
    }
}
