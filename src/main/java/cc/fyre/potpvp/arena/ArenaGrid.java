/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.sk89q.worldedit.CuboidClipboard
 *  com.sk89q.worldedit.Vector
 *  org.bukkit.Location
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  rip.bridge.qlib.cuboid.Cuboid
 */
package cc.fyre.potpvp.arena;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.Arena;
import cc.fyre.potpvp.arena.ArenaHandler;
import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.arena.WorldEditUtils;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.Vector;
import java.io.IOException;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import rip.bridge.qlib.cuboid.Cuboid;

public final class ArenaGrid {
    public static final Vector STARTING_POINT = new Vector(1000, 80, 1000);
    public static final int GRID_SPACING_X = 300;
    public static final int GRID_SPACING_Z = 300;
    private boolean busy = false;

    public void scaleCopies(ArenaSchematic schematic, int desiredCopies, Runnable callback) {
        if (this.busy) {
            throw new IllegalStateException("Grid is busy!");
        }
        this.busy = true;
        ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
        int currentCopies = arenaHandler.countArenas(schematic);
        Runnable saveWrapper = () -> {
            try {
                arenaHandler.saveArenas();
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            this.busy = false;
            callback.run();
        };
        if (currentCopies > desiredCopies) {
            this.deleteArenas(schematic, currentCopies, currentCopies - desiredCopies, saveWrapper);
        } else if (currentCopies < desiredCopies) {
            this.createArenas(schematic, currentCopies, desiredCopies - currentCopies, saveWrapper);
        } else {
            saveWrapper.run();
        }
    }

    private void createArenas(final ArenaSchematic schematic, final int currentCopies, final int toCreate, final Runnable callback) {
        final ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
        new BukkitRunnable(){
            int created = 0;

            public void run() {
                int copy2 = currentCopies + this.created + 1;
                int xStart = STARTING_POINT.getBlockX() + 300 * schematic.getGridIndex();
                int zStart = STARTING_POINT.getBlockZ() + 300 * copy2;
                try {
                    Arena created = ArenaGrid.this.createArena(schematic, xStart, zStart, copy2);
                    arenaHandler.registerArena(created);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    callback.run();
                    this.cancel();
                }
                ++this.created;
                if (this.created == toCreate) {
                    callback.run();
                    this.cancel();
                }
            }
        }.runTaskTimer((Plugin)PotPvP.getInstance(), 8L, 8L);
    }

    private void deleteArenas(final ArenaSchematic schematic, final int currentCopies, final int toDelete, final Runnable callback) {
        final ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
        new BukkitRunnable(){
            int deleted = 0;

            public void run() {
                int copy2 = currentCopies - this.deleted;
                Arena existing = arenaHandler.getArena(schematic, copy2);
                if (existing != null) {
                    WorldEditUtils.clear(existing.getBounds());
                    arenaHandler.unregisterArena(existing);
                }
                ++this.deleted;
                if (this.deleted == toDelete) {
                    callback.run();
                    this.cancel();
                }
            }
        }.runTaskTimer((Plugin)PotPvP.getInstance(), 8L, 8L);
    }

    private Arena createArena(ArenaSchematic schematic, int xStart, int zStart, int copy2) {
        CuboidClipboard clipboard;
        Vector pasteAt = new Vector((double)xStart, STARTING_POINT.getY(), (double)zStart);
        try {
            clipboard = WorldEditUtils.paste(schematic, pasteAt);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        Location lowerCorner = WorldEditUtils.vectorToLocation(pasteAt);
        Location upperCorner = WorldEditUtils.vectorToLocation(pasteAt.add(clipboard.getSize()));
        return new Arena(schematic.getName(), copy2, new Cuboid(lowerCorner, upperCorner));
    }

    public void free() {
        this.busy = false;
    }

    public boolean isBusy() {
        return this.busy;
    }
}

