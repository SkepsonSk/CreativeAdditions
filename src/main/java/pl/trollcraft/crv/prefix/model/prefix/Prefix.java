package pl.trollcraft.crv.prefix.model.prefix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public final class Prefix {

    private final int id;

    private String name;
    private String noColorsName;

    private PrefixType type;
    private double price;

    public Prefix(int id,
                  String name,
                  PrefixType type,
                  double price) {

        this.id = id;

        this.name = ChatColor.translateAlternateColorCodes('&', name);
        noColorsName = name.replaceAll("(&|§)[1-9|a-z]", "");

        this.type = type;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameNoColors() {
        return noColorsName;
    }

    public PrefixType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = ChatColor.translateAlternateColorCodes('&', name).replaceAll("_", " ");
        noColorsName = name.replaceAll("(&|§)[1-9|a-z]", "");
    }

    public void setType(PrefixType type) {
        this.type = type;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
