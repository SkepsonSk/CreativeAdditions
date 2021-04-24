package pl.trollcraft.crv.we.config;

import pl.trollcraft.crv.config.ConfigProvider;
import pl.trollcraft.crv.config.config.Config;
import pl.trollcraft.crv.we.model.WorldEditRestriction;

import java.util.List;

public class WorldEditConfig implements Config {

    private final WorldEditRestriction worldEditRestriction;

    public WorldEditConfig(WorldEditRestriction worldEditRestriction) {
        this.worldEditRestriction = worldEditRestriction;
    }

    @Override
    public void configure(ConfigProvider provider) {
        List<String> commands = provider.conf().getStringList("commands");
        List<String> forbidden = provider.conf().getStringList("forbidden");

        worldEditRestriction.setLimitedCommands(commands);
        worldEditRestriction.setForbiddenMaterials(forbidden);

        int maxDefault = provider.read("selection.max.default", Integer.class);
        int maxVip = provider.read("selection.max.vip", Integer.class);
        int maxSvip = provider.read("selection.max.svip", Integer.class);
        int maxMvip = provider.read("selection.max.mvip", Integer.class);

        worldEditRestriction.setMaxDefault(maxDefault);
        worldEditRestriction.setMaxVip(maxVip);
        worldEditRestriction.setMaxSVip(maxSvip);
        worldEditRestriction.setMaxMVip(maxMvip);

        int start = provider.read("start", Integer.class);

        worldEditRestriction.setStart(start);

        int cooldownDefault = provider.read("cooldown.default", Integer.class);
        int cooldownVip = provider.read("cooldown.vip", Integer.class);
        int cooldownSvip = provider.read("cooldown.svip", Integer.class);
        int cooldownMvip = provider.read("cooldown.mvip", Integer.class);

        worldEditRestriction.setCooldownDefault(cooldownDefault);
        worldEditRestriction.setCooldownVip(cooldownVip);
        worldEditRestriction.setCooldownSVip(cooldownSvip);
        worldEditRestriction.setCooldownMVip(cooldownMvip);
    }
}
