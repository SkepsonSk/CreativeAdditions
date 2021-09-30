package pl.trollcraft.crv.config.config;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import pl.trollcraft.crv.config.ConfigProvider;
import pl.trollcraft.crv.entityClear.EntityClearController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityClearConfig implements Config {

    private final EntityClearController entityClearController;

    public EntityClearConfig(EntityClearController entityClearController) {
        this.entityClearController = entityClearController;
    }

    @Override
    public void configure(ConfigProvider provider) {
        List<String> worldNames = provider.conf().getStringList("worlds");
        Set<World> worlds = worldNames.stream()
                .map(Bukkit::getWorld)
                .collect(Collectors.toSet());

        List<String> entityTypeNames = provider.conf().getStringList("entity-types");
        Set<EntityType> entityTypes = entityTypeNames.stream()
                .map( EntityType::valueOf )
                .collect(Collectors.toSet());

        long interval = provider.read("clearing-interval", Integer.class);

        entityClearController.setWorldsToClear(worlds);
        entityClearController.setEntityTypesToClear(entityTypes);
        entityClearController.setClearingTaskInterval(interval);
    }



}
