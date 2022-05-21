/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.io.Files
 *  com.google.gson.reflect.TypeToken
 *  com.sk89q.worldedit.bukkit.WorldEditPlugin
 *  org.bukkit.Bukkit
 *  org.bukkit.World
 *  org.bukkit.event.Event
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package cc.fyre.potpvp.arena;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.Arena;
import cc.fyre.potpvp.arena.ArenaGrid;
import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.arena.event.ArenaAllocatedEvent;
import cc.fyre.potpvp.arena.event.ArenaReleasedEvent;
import cc.fyre.potpvp.arena.listener.ArenaItemResetListener;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ArenaHandler {
    public static final File WORLD_EDIT_SCHEMATICS_FOLDER = new File(((WorldEditPlugin)JavaPlugin.getPlugin(WorldEditPlugin.class)).getDataFolder(), "schematics");
    private static final String ARENA_INSTANCES_FILE_NAME = "arenaInstances.json";
    private static final String SCHEMATICS_FILE_NAME = "schematics.json";
    private final Map<String, Map<Integer, Arena>> arenaInstances;
    private final Map<String, ArenaSchematic> schematics;
    private final ArenaGrid grid;

    public ArenaHandler() {
        block18: {
            this.arenaInstances = new HashMap<String, Map<Integer, Arena>>();
            this.schematics = new TreeMap<String, ArenaSchematic>();
            this.grid = new ArenaGrid();
            Bukkit.getPluginManager().registerEvents((Listener)new ArenaItemResetListener(), (Plugin)PotPvP.getInstance());
            File worldFolder = this.getArenaWorld().getWorldFolder();
            File arenaInstancesFile = new File(worldFolder, ARENA_INSTANCES_FILE_NAME);
            File schematicsFile = new File(worldFolder, SCHEMATICS_FILE_NAME);
            try {
                if (arenaInstancesFile.exists()) {
                    try (BufferedReader arenaInstancesReader = Files.newReader((File)arenaInstancesFile, (Charset)Charsets.UTF_8);){
                        Type arenaListType = new TypeToken<List<Arena>>(){}.getType();
                        List<Arena> arenaList = (List)PotPvP.gson.fromJson((Reader)arenaInstancesReader, arenaListType);
                        for (Arena arena : arenaList) {
                            this.arenaInstances.computeIfAbsent(arena.getSchematic(), i -> new HashMap());
                            this.arenaInstances.get(arena.getSchematic()).put(arena.getCopy(), arena);
                        }
                    }
                }
                if (!schematicsFile.exists()) break block18;
                try (BufferedReader schematicsFileReader = Files.newReader((File)schematicsFile, (Charset)Charsets.UTF_8);){
                    Type schematicListType = new TypeToken<List<ArenaSchematic>>(){}.getType();
                    List<ArenaSchematic> schematicList = (List)PotPvP.gson.fromJson((Reader)schematicsFileReader, schematicListType);
                    for (ArenaSchematic schematic : schematicList) {
                        this.schematics.put(schematic.getName(), schematic);
                    }
                }
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void saveSchematics() throws IOException {
        Files.write((CharSequence)PotPvP.gson.toJson(this.schematics.values()), (File)new File(this.getArenaWorld().getWorldFolder(), SCHEMATICS_FILE_NAME), (Charset)Charsets.UTF_8);
    }

    public void saveArenas() throws IOException {
        ArrayList allArenas = new ArrayList();
        this.arenaInstances.forEach((schematic, copies) -> allArenas.addAll(copies.values()));
        Files.write((CharSequence)PotPvP.gson.toJson(allArenas), (File)new File(this.getArenaWorld().getWorldFolder(), ARENA_INSTANCES_FILE_NAME), (Charset)Charsets.UTF_8);
    }

    public World getArenaWorld() {
        return (World)Bukkit.getWorlds().get(0);
    }

    public void registerSchematic(ArenaSchematic schematic) {
        int lastGridIndex = 0;
        for (ArenaSchematic otherSchematic : this.schematics.values()) {
            lastGridIndex = Math.max(lastGridIndex, otherSchematic.getGridIndex());
        }
        schematic.setGridIndex(lastGridIndex + 1);
        this.schematics.put(schematic.getName(), schematic);
    }

    public void unregisterSchematic(ArenaSchematic schematic) {
        this.schematics.remove(schematic.getName());
    }

    void registerArena(Arena arena) {
        Map<Integer, Arena> copies = this.arenaInstances.get(arena.getSchematic());
        if (copies == null) {
            copies = new HashMap<Integer, Arena>();
            this.arenaInstances.put(arena.getSchematic(), copies);
        }
        copies.put(arena.getCopy(), arena);
    }

    void unregisterArena(Arena arena) {
        Map<Integer, Arena> copies = this.arenaInstances.get(arena.getSchematic());
        if (copies != null) {
            copies.remove(arena.getCopy());
        }
    }

    public Arena getArena(ArenaSchematic schematic, int copy2) {
        Map<Integer, Arena> arenaCopies = this.arenaInstances.get(schematic.getName());
        if (arenaCopies != null) {
            return arenaCopies.get(copy2);
        }
        return null;
    }

    public Set<Arena> getArenas(ArenaSchematic schematic) {
        Map<Integer, Arena> arenaCopies = this.arenaInstances.get(schematic.getName());
        if (arenaCopies != null) {
            return ImmutableSet.copyOf(arenaCopies.values());
        }
        return ImmutableSet.of();
    }

    public int countArenas(ArenaSchematic schematic) {
        Map<Integer, Arena> arenaCopies = this.arenaInstances.get(schematic.getName());
        return arenaCopies != null ? arenaCopies.size() : 0;
    }

    public Set<ArenaSchematic> getSchematics() {
        return ImmutableSet.copyOf(this.schematics.values());
    }

    public ArenaSchematic getSchematic(String schematicName) {
        return this.schematics.get(schematicName);
    }

    public List<ArenaSchematic> findArenaSchematics(Predicate<ArenaSchematic> acceptableSchematicPredicate) {
        ArrayList<ArenaSchematic> acceptableArenaSchematics = new ArrayList<ArenaSchematic>();
        for (ArenaSchematic schematic : this.schematics.values()) {
            if (!acceptableSchematicPredicate.test(schematic)) continue;
            acceptableArenaSchematics.add(schematic);
        }
        return acceptableArenaSchematics;
    }

    public Optional<Arena> allocateUnusedArena(Predicate<ArenaSchematic> acceptableSchematicPredicate) {
        ArrayList<Arena> acceptableArenas = new ArrayList<Arena>();
        for (ArenaSchematic schematic : this.schematics.values()) {
            if (!acceptableSchematicPredicate.test(schematic) || !this.arenaInstances.containsKey(schematic.getName())) continue;
            for (Arena arena : this.arenaInstances.get(schematic.getName()).values()) {
                if (arena.isInUse()) continue;
                acceptableArenas.add(arena);
            }
        }
        if (acceptableArenas.isEmpty()) {
            return Optional.empty();
        }
        Arena selected = (Arena)acceptableArenas.get(ThreadLocalRandom.current().nextInt(acceptableArenas.size()));
        selected.setInUse(true);
        Bukkit.getPluginManager().callEvent((Event)new ArenaAllocatedEvent(selected));
        return Optional.of(selected);
    }

    public void releaseArena(Arena arena) {
        Preconditions.checkArgument((boolean)arena.isInUse(), (Object)"Cannot release arena not in use.");
        arena.setInUse(false);
        Bukkit.getPluginManager().callEvent((Event)new ArenaReleasedEvent(arena));
    }

    public ArenaGrid getGrid() {
        return this.grid;
    }
}

