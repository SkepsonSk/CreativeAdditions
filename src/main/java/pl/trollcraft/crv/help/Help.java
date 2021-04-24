package pl.trollcraft.crv.help;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Help {

    public static String locationToString(Location loc) {

        String world = Objects.requireNonNull(loc.getWorld()).getName();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();

        return String.format("%s;%f;%f;%f;%f;%f",
                world, x, y, z, yaw, pitch);

    }

    public static Location locationFromString(String s) {

        String[] sA = s.split(";");
        if (sA.length != 6)
            throw new IllegalStateException("Invalid loc string format (" + sA.length + ").");

        World world = Bukkit.getWorld(sA[0]);
        double x = Double.parseDouble(sA[1]);
        double y = Double.parseDouble(sA[2]);
        double z = Double.parseDouble(sA[3]);
        float yaw = Float.parseFloat(sA[4]);
        float pitch = Float.parseFloat(sA[5]);

        return new Location(world, x, y, z, yaw, pitch);

    }

    public static void send(CommandSender player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                message));
    }

}