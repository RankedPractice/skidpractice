/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Maps
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.Skull
 *  org.bukkit.craftbukkit.v1_7_R4.util.LongHash
 *  rip.bridge.ChunkSnapshot
 *  rip.bridge.qlib.cuboid.Cuboid
 *  rip.bridge.qlib.util.Callback
 */
package cc.fyre.potpvp.arena;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.ArenaHandler;
import cc.fyre.potpvp.util.AngleUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_7_R4.util.LongHash;
import rip.bridge.ChunkSnapshot;
import rip.bridge.qlib.cuboid.Cuboid;
import rip.bridge.qlib.util.Callback;

public final class Arena {
    private String schematic;
    private int copy;
    private Cuboid bounds;
    private Location team1Spawn;
    private Location team2Spawn;
    private Location blueSpawn;
    private Location redSpawn;
    private List<Location> bedLocs = new ArrayList<Location>();
    private Location spectatorSpawn;
    private List<Location> eventSpawns;
    private transient boolean inUse;
    private final transient Map<Long, ChunkSnapshot> chunkSnapshots = Maps.newHashMap();

    public Arena() {
    }

    public Arena(String schematic, int copy2, Cuboid bounds) {
        this.schematic = (String)Preconditions.checkNotNull(schematic);
        this.copy = copy2;
        this.bounds = (Cuboid)Preconditions.checkNotNull(bounds);
        this.scanLocations();
    }

    public Location getSpectatorSpawn() {
        if (this.spectatorSpawn != null) {
            return this.spectatorSpawn;
        }
        int xDiff = Math.abs(this.team1Spawn.getBlockX() - this.team2Spawn.getBlockX());
        int yDiff = Math.abs(this.team1Spawn.getBlockY() - this.team2Spawn.getBlockY());
        int zDiff = Math.abs(this.team1Spawn.getBlockZ() - this.team2Spawn.getBlockZ());
        int newX = Math.min(this.team1Spawn.getBlockX(), this.team2Spawn.getBlockX()) + xDiff / 2;
        int newY = Math.min(this.team1Spawn.getBlockY(), this.team2Spawn.getBlockY()) + yDiff / 2;
        int newZ = Math.min(this.team1Spawn.getBlockZ(), this.team2Spawn.getBlockZ()) + zDiff / 2;
        ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
        this.spectatorSpawn = new Location(arenaHandler.getArenaWorld(), (double)newX, (double)newY, (double)newZ);
        while (this.spectatorSpawn.getBlock().getType().isSolid()) {
            this.spectatorSpawn = this.spectatorSpawn.add(0.0, 1.0, 0.0);
        }
        return this.spectatorSpawn;
    }

    private void scanLocations() {
        int[] i = new int[]{0};
        this.forEachBlock(block -> {
            Material type2 = block.getType();
            if (type2 == Material.WOOL) {
                Block below;
                if (block.getData() == 14) {
                    below = block.getRelative(BlockFace.DOWN);
                    if (below.getType() != Material.FENCE) {
                        return;
                    }
                    block.setType(Material.AIR);
                    below.setType(Material.AIR);
                    this.redSpawn = block.getLocation().clone().add(0.5, 1.5, 0.5);
                }
                if (block.getData() == 11) {
                    below = block.getRelative(BlockFace.DOWN);
                    if (below.getType() != Material.FENCE) {
                        return;
                    }
                    block.setType(Material.AIR);
                    below.setType(Material.AIR);
                    this.blueSpawn = block.getLocation().clone().add(0.5, 1.5, 0.5);
                }
            }
            if (type2 == Material.BED) {
                this.bedLocs.add(block.getLocation());
                return;
            }
            if (type2 != Material.SKULL) {
                return;
            }
            Skull skull = (Skull)block.getState();
            Block below = block.getRelative(BlockFace.DOWN);
            Location skullLocation = block.getLocation().clone().add(0.5, 1.5, 0.5);
            skullLocation.setYaw((float)(AngleUtils.faceToYaw(skull.getRotation()) + 90));
            switch (skull.getSkullType()) {
                case SKELETON: {
                    this.spectatorSpawn = skullLocation;
                    block.setType(Material.AIR);
                    if (below.getType() != Material.FENCE) break;
                    below.setType(Material.AIR);
                    break;
                }
                case PLAYER: {
                    if (this.team1Spawn == null) {
                        this.team1Spawn = skullLocation;
                    } else {
                        this.team2Spawn = skullLocation;
                    }
                    block.setType(Material.AIR);
                    if (below.getType() != Material.FENCE) break;
                    below.setType(Material.AIR);
                    break;
                }
                case CREEPER: {
                    block.setType(Material.AIR);
                    if (below.getType() == Material.FENCE) {
                        below.setType(Material.AIR);
                    }
                    if (this.eventSpawns == null) {
                        this.eventSpawns = new ArrayList<Location>();
                    }
                    if (this.eventSpawns.contains(skullLocation)) break;
                    this.eventSpawns.add(skullLocation);
                }
            }
        });
        Preconditions.checkNotNull(this.team1Spawn, "Team 1 spawn (player skull) cannot be null.");
        Preconditions.checkNotNull(this.team2Spawn, "Team 2 spawn (player skull) cannot be null.");
    }

    private void forEachBlock(Callback<Block> callback) {
        Location start = this.bounds.getLowerNE();
        Location end = this.bounds.getUpperSW();
        World world = this.bounds.getWorld();
        for (int x = start.getBlockX(); x < end.getBlockX(); ++x) {
            for (int y = start.getBlockY(); y < end.getBlockY(); ++y) {
                for (int z = start.getBlockZ(); z < end.getBlockZ(); ++z) {
                    callback.callback(world.getBlockAt(x, y, z));
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void takeSnapshot() {
        Map<Long, ChunkSnapshot> map = this.chunkSnapshots;
        synchronized (map) {
            this.forEachChunk(chunk -> this.chunkSnapshots.put(LongHash.toLong((int)chunk.getX(), (int)chunk.getZ()), chunk.takeSnapshot()));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void restore() {
        Map<Long, ChunkSnapshot> map = this.chunkSnapshots;
        synchronized (map) {
            World world = this.bounds.getWorld();
            this.chunkSnapshots.entrySet().forEach(entry -> world.getChunkAt(LongHash.msw((long)((Long)entry.getKey())), LongHash.lsw((long)((Long)entry.getKey()))).restoreSnapshot((ChunkSnapshot)entry.getValue()));
        }
    }

    private void forEachChunk(Callback<Chunk> callback) {
        int lowerX = this.bounds.getLowerX() >> 4;
        int lowerZ = this.bounds.getLowerZ() >> 4;
        int upperX = this.bounds.getUpperX() >> 4;
        int upperZ = this.bounds.getUpperZ() >> 4;
        World world = this.bounds.getWorld();
        for (int x = lowerX; x <= upperX; ++x) {
            for (int z = lowerZ; z <= upperZ; ++z) {
                callback.callback(world.getChunkAt(x, z));
            }
        }
    }

    public boolean equals(Object o) {
        if (o instanceof Arena) {
            if (this.schematic == null) {
                return false;
            }
            Arena a = (Arena)o;
            return a.schematic.equals(this.schematic) && a.copy == this.copy;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.schematic, this.copy);
    }

    public Location getTeam1Spawn() {
        return this.team1Spawn;
    }

    public Location getTeam2Spawn() {
        return this.team2Spawn;
    }

    public Location getRedSpawn() {
        return this.redSpawn;
    }

    public Location getBlueSpawnSpawn() {
        return this.blueSpawn;
    }

    public List<Location> getBedLocs() {
        return this.bedLocs;
    }

    public List<Location> getEventSpawns() {
        return this.eventSpawns;
    }

    public Cuboid getBounds() {
        return this.bounds;
    }

    public String getSchematic() {
        return this.schematic;
    }

    public int getCopy() {
        return this.copy;
    }

    public boolean isInUse() {
        return this.inUse;
    }

    void setInUse(boolean inUse) {
        this.inUse = inUse;
    }
}

