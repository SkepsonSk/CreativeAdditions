package pl.trollcraft.crv.entityClear;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class EntityClearService {

    private final Plugin plugin;
    private BukkitTask clearingTask;
    private Multimap<EntityType, Predicate<Entity>> removalPredicates;

    private final EntityClearController entityClearController;

    public EntityClearService(Plugin plugin,
                              EntityClearController entityClearController) {

        this.plugin = plugin;
        this.entityClearController = entityClearController;
        this.removalPredicates = ArrayListMultimap.create();

        this.removalPredicates.put(EntityType.ARMOR_STAND, entity -> !entity.getName().contains(";"));
    }

    public int clearEntities() {

        AtomicInteger count = new AtomicInteger(0);

        this.entityClearController.getWorldsToClear().forEach( world -> world.getEntities().forEach(entity -> {

            EntityType type = entity.getType();
            if (!this.entityClearController.getEntityTypesToClear().contains(type)) {
                return;
            }

            boolean shouldRemove = true;

            Collection<Predicate<Entity>> predicates = removalPredicates.get(entity.getType());
            for (Predicate<Entity> predicate : predicates) {
                if (!predicate.test(entity)) {
                    shouldRemove = false;
                    break;
                }
            }

            if (shouldRemove) {
                entity.remove();
                count.incrementAndGet();
            }

        }));

        return count.get();

    }

    public void setupClearing() {

        clearingTask = new BukkitRunnable() {

            @Override
            public void run() {

            }

        }.runTaskTimer(this.plugin,
                this.entityClearController.getClearingTaskInterval(),
                this.entityClearController.getClearingTaskInterval());

    }

}
