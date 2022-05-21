/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 */
package cc.fyre.potpvp.match;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public final class MatchTeam {
    private final int id;
    private final Set<UUID> allMembers;
    boolean bedBroken = false;
    private Location spawnLoc;
    private final Set<UUID> aliveMembers = Collections.newSetFromMap(new ConcurrentHashMap());
    private int points = 0;

    public MatchTeam(int id, UUID initialMember) {
        this(id, (Collection<UUID>)ImmutableSet.of((Object)initialMember));
    }

    public MatchTeam(int id, Collection<UUID> initialMembers) {
        this.id = id;
        this.allMembers = ImmutableSet.copyOf(initialMembers);
        this.aliveMembers.addAll(initialMembers);
    }

    public void markDead(UUID playerUuid) {
        this.aliveMembers.remove(playerUuid);
    }

    public void markAlive(UUID playerUuid) {
        this.aliveMembers.add(playerUuid);
    }

    public boolean isAlive(UUID playerUuid) {
        return this.aliveMembers.contains(playerUuid);
    }

    public Set<UUID> getAliveMembers() {
        return ImmutableSet.copyOf(this.aliveMembers);
    }

    public UUID getFirstAliveMember() {
        return this.aliveMembers.iterator().next();
    }

    public UUID getFirstMember() {
        return this.allMembers.iterator().next();
    }

    public void messageAlive(String message) {
        this.forEachAlive(p -> p.sendMessage(message));
    }

    public void playSoundAlive(Sound sound, float pitch) {
        this.forEachAlive(p -> p.playSound(p.getLocation(), sound, 10.0f, pitch));
    }

    public int getId() {
        return this.id;
    }

    public void incrementPoints() {
        ++this.points;
    }

    public int getPoints() {
        return this.points;
    }

    private void forEachAlive(Consumer<Player> consumer) {
        for (UUID member : this.aliveMembers) {
            Player memberBukkit = Bukkit.getPlayer((UUID)member);
            if (memberBukkit == null) continue;
            consumer.accept(memberBukkit);
        }
    }

    public Set<UUID> getAllMembers() {
        return this.allMembers;
    }

    public void setBedBroken(boolean bedBroken) {
        this.bedBroken = bedBroken;
    }

    public boolean isBedBroken() {
        return this.bedBroken;
    }

    public Location getSpawnLoc() {
        return this.spawnLoc;
    }

    public void setSpawnLoc(Location spawnLoc) {
        this.spawnLoc = spawnLoc;
    }
}

