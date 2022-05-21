/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Item
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  rip.bridge.qlib.cuboid.Cuboid
 */
package cc.fyre.potpvp.arena.listener;

import cc.fyre.potpvp.arena.event.ArenaReleasedEvent;
import java.util.HashSet;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import rip.bridge.qlib.cuboid.Cuboid;

public final class ArenaItemResetListener
implements Listener {
    @EventHandler
    public void onArenaReleased(ArenaReleasedEvent event) {
        HashSet<Chunk> coveredChunks = new HashSet<Chunk>();
        Cuboid bounds = event.getArena().getBounds();
        Location minPoint = bounds.getLowerNE();
        Location maxPoint = bounds.getUpperSW();
        World world = minPoint.getWorld();
        for (int x = minPoint.getBlockX(); x <= maxPoint.getBlockX(); ++x) {
            for (int z = minPoint.getBlockZ(); z <= maxPoint.getBlockZ(); ++z) {
                coveredChunks.add(world.getChunkAt(x >> 4, z >> 4));
            }
        }
        coveredChunks.forEach(Chunk::load);
        coveredChunks.forEach(chunk -> {
            for (Entity entity : chunk.getEntities()) {
                if (!(entity instanceof Item) || !bounds.contains(entity.getLocation())) continue;
                entity.remove();
            }
        });
    }
}

