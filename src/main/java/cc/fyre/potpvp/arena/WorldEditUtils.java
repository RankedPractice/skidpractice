/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.sk89q.worldedit.CuboidClipboard
 *  com.sk89q.worldedit.EditSession
 *  com.sk89q.worldedit.EditSessionFactory
 *  com.sk89q.worldedit.MaxChangedBlocksException
 *  com.sk89q.worldedit.Vector
 *  com.sk89q.worldedit.WorldEdit
 *  com.sk89q.worldedit.blocks.BaseBlock
 *  com.sk89q.worldedit.bukkit.BukkitWorld
 *  com.sk89q.worldedit.regions.CuboidRegion
 *  com.sk89q.worldedit.regions.Region
 *  com.sk89q.worldedit.schematic.SchematicFormat
 *  com.sk89q.worldedit.world.World
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  rip.bridge.qlib.cuboid.Cuboid
 */
package cc.fyre.potpvp.arena;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.ArenaHandler;
import cc.fyre.potpvp.arena.ArenaSchematic;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EditSessionFactory;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.world.World;
import java.io.File;
import org.bukkit.Location;
import org.bukkit.Material;
import rip.bridge.qlib.cuboid.Cuboid;

public final class WorldEditUtils {
    private static EditSession editSession;
    private static World worldEditWorld;

    public static void primeWorldEditApi() {
        if (editSession != null) {
            return;
        }
        EditSessionFactory esFactory = WorldEdit.getInstance().getEditSessionFactory();
        ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
        worldEditWorld = new BukkitWorld(arenaHandler.getArenaWorld());
        editSession = esFactory.getEditSession(worldEditWorld, Integer.MAX_VALUE);
    }

    public static CuboidClipboard paste(ArenaSchematic schematic, Vector pasteAt) throws Exception {
        WorldEditUtils.primeWorldEditApi();
        CuboidClipboard clipboard = SchematicFormat.MCEDIT.load(schematic.getSchematicFile());
        clipboard.setOffset(new Vector(0, 0, 0));
        clipboard.paste(editSession, pasteAt, true);
        return clipboard;
    }

    public static void save(ArenaSchematic schematic, Vector saveFrom) throws Exception {
        WorldEditUtils.primeWorldEditApi();
        Vector schematicSize = WorldEditUtils.readSchematicSize(schematic);
        CuboidClipboard newSchematic = new CuboidClipboard(schematicSize, saveFrom);
        newSchematic.copy(editSession);
        SchematicFormat.MCEDIT.save(newSchematic, schematic.getSchematicFile());
    }

    public static void clear(Cuboid bounds) {
        WorldEditUtils.clear(new Vector(bounds.getLowerX(), bounds.getLowerY(), bounds.getLowerZ()), new Vector(bounds.getUpperX(), bounds.getUpperY(), bounds.getUpperZ()));
    }

    public static void clear(Vector lower, Vector upper) {
        WorldEditUtils.primeWorldEditApi();
        BaseBlock air = new BaseBlock(Material.AIR.getId());
        CuboidRegion region = new CuboidRegion(worldEditWorld, lower, upper);
        try {
            editSession.setBlocks((Region)region, air);
        }
        catch (MaxChangedBlocksException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Vector readSchematicSize(ArenaSchematic schematic) throws Exception {
        File schematicFile = schematic.getSchematicFile();
        CuboidClipboard clipboard = SchematicFormat.MCEDIT.load(schematicFile);
        return clipboard.getSize();
    }

    public static Location vectorToLocation(Vector vector) {
        ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
        return new Location(arenaHandler.getArenaWorld(), (double)vector.getBlockX(), (double)vector.getBlockY(), (double)vector.getBlockZ());
    }

    private WorldEditUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

