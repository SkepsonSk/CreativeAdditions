package pl.trollcraft.crv.entityClear;

import org.bukkit.World;
import org.bukkit.entity.EntityType;

import java.util.Set;

public class EntityClearController {

    private Set<EntityType> entityTypesToClear;
    private Set<World> worldsToClear;
    private long clearingTaskInterval;

    public Set<EntityType> getEntityTypesToClear() {
        return entityTypesToClear;
    }

    public void setEntityTypesToClear(Set<EntityType> entityTypesToClear) {
        this.entityTypesToClear = entityTypesToClear;
    }

    public Set<World> getWorldsToClear() {
        return worldsToClear;
    }

    public void setWorldsToClear(Set<World> worldsToClear) {
        this.worldsToClear = worldsToClear;
    }

    public long getClearingTaskInterval() {
        return clearingTaskInterval;
    }

    public void setClearingTaskInterval(long clearingTaskInterval) {
        this.clearingTaskInterval = clearingTaskInterval;
    }
}
