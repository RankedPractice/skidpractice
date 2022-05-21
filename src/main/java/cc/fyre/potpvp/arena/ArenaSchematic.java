/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.sk89q.worldedit.Vector
 */
package cc.fyre.potpvp.arena;

import cc.fyre.potpvp.arena.ArenaGrid;
import cc.fyre.potpvp.arena.ArenaHandler;
import cc.fyre.potpvp.arena.WorldEditUtils;
import cc.fyre.potpvp.game.event.GameEvent;
import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import java.io.File;

public final class ArenaSchematic {
    public String name;
    private boolean enabled = false;
    private int maxPlayerCount = 256;
    private boolean bridgesOnly = false;
    private boolean bedFightsOnly = false;
    private boolean mlgOnly = false;
    private int minPlayerCount = 2;
    private boolean supportsRanked = false;
    private boolean archerOnly = false;
    private boolean teamFightsOnly = false;
    private boolean sumoOnly = false;
    private boolean spleefOnly = false;
    private boolean buildUHCOnly = false;
    private boolean HCFOnly = false;
    private String eventName = null;
    private int gridIndex;

    public ArenaSchematic() {
    }

    public ArenaSchematic(String name) {
        this.name = (String)Preconditions.checkNotNull((Object)name, (Object)"name");
    }

    public File getSchematicFile() {
        return new File(ArenaHandler.WORLD_EDIT_SCHEMATICS_FOLDER, this.name + ".schematic");
    }

    public Vector getModelArenaLocation() {
        int xModifier = 300 * this.gridIndex;
        return new Vector(ArenaGrid.STARTING_POINT.getBlockX() - xModifier, ArenaGrid.STARTING_POINT.getBlockY(), ArenaGrid.STARTING_POINT.getBlockZ());
    }

    public void pasteModelArena() throws Exception {
        Vector start = this.getModelArenaLocation();
        WorldEditUtils.paste(this, start);
    }

    public void removeModelArena() throws Exception {
        Vector start = this.getModelArenaLocation();
        Vector size = WorldEditUtils.readSchematicSize(this);
        WorldEditUtils.clear(start, start.add(size));
    }

    public GameEvent getEvent() {
        if (this.eventName != null) {
            for (GameEvent event : GameEvent.getEvents()) {
                if (!event.getName().equalsIgnoreCase(this.eventName)) continue;
                return event;
            }
            this.eventName = null;
        }
        return null;
    }

    public boolean equals(Object o) {
        return o instanceof ArenaSchematic && ((ArenaSchematic)o).name.equals(this.name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getName() {
        return this.name;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getMaxPlayerCount() {
        return this.maxPlayerCount;
    }

    public void setMaxPlayerCount(int maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public boolean isBridgesOnly() {
        return this.bridgesOnly;
    }

    public void setBridgesOnly(boolean bridgesOnly) {
        this.bridgesOnly = bridgesOnly;
    }

    public boolean isBedFightsOnly() {
        return this.bedFightsOnly;
    }

    public void setBedFightsOnly(boolean bedFightsOnly) {
        this.bedFightsOnly = bedFightsOnly;
    }

    public boolean isMlgOnly() {
        return this.mlgOnly;
    }

    public void setMlgOnly(boolean mlgOnly) {
        this.mlgOnly = mlgOnly;
    }

    public int getMinPlayerCount() {
        return this.minPlayerCount;
    }

    public void setMinPlayerCount(int minPlayerCount) {
        this.minPlayerCount = minPlayerCount;
    }

    public boolean isSupportsRanked() {
        return this.supportsRanked;
    }

    public void setSupportsRanked(boolean supportsRanked) {
        this.supportsRanked = supportsRanked;
    }

    public boolean isArcherOnly() {
        return this.archerOnly;
    }

    public void setArcherOnly(boolean archerOnly) {
        this.archerOnly = archerOnly;
    }

    public boolean isTeamFightsOnly() {
        return this.teamFightsOnly;
    }

    public void setTeamFightsOnly(boolean teamFightsOnly) {
        this.teamFightsOnly = teamFightsOnly;
    }

    public boolean isSumoOnly() {
        return this.sumoOnly;
    }

    public void setSumoOnly(boolean sumoOnly) {
        this.sumoOnly = sumoOnly;
    }

    public boolean isSpleefOnly() {
        return this.spleefOnly;
    }

    public void setSpleefOnly(boolean spleefOnly) {
        this.spleefOnly = spleefOnly;
    }

    public boolean isBuildUHCOnly() {
        return this.buildUHCOnly;
    }

    public void setBuildUHCOnly(boolean buildUHCOnly) {
        this.buildUHCOnly = buildUHCOnly;
    }

    public boolean isHCFOnly() {
        return this.HCFOnly;
    }

    public void setHCFOnly(boolean HCFOnly) {
        this.HCFOnly = HCFOnly;
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getGridIndex() {
        return this.gridIndex;
    }

    public void setGridIndex(int gridIndex) {
        this.gridIndex = gridIndex;
    }
}

