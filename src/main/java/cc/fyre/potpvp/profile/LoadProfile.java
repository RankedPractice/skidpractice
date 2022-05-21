/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.scheduler.BukkitTask
 */
package cc.fyre.potpvp.profile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.scheduler.BukkitTask;

public class LoadProfile {
    public static Map<UUID, LoadProfile> profileMap = new HashMap<UUID, LoadProfile>();
    private final UUID uuid;
    private String lastHitName = "";
    private String lastDamagerName = "";
    private BukkitTask ninjaTask;

    public LoadProfile(UUID uuid) {
        this.uuid = uuid;
        profileMap.put(this.uuid, this);
    }

    public static LoadProfile byUUID(UUID toSearch) {
        for (LoadProfile value : profileMap.values()) {
            if (value.getUuid() != toSearch) continue;
            return value;
        }
        return new LoadProfile(toSearch);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getLastHitName() {
        return this.lastHitName;
    }

    public String getLastDamagerName() {
        return this.lastDamagerName;
    }

    public BukkitTask getNinjaTask() {
        return this.ninjaTask;
    }

    public void setLastHitName(String lastHitName) {
        this.lastHitName = lastHitName;
    }

    public void setLastDamagerName(String lastDamagerName) {
        this.lastDamagerName = lastDamagerName;
    }

    public void setNinjaTask(BukkitTask ninjaTask) {
        this.ninjaTask = ninjaTask;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LoadProfile)) {
            return false;
        }
        LoadProfile other = (LoadProfile)o;
        if (!other.canEqual(this)) {
            return false;
        }
        UUID this$uuid = this.getUuid();
        UUID other$uuid = other.getUuid();
        if (this$uuid == null ? other$uuid != null : !((Object)this$uuid).equals(other$uuid)) {
            return false;
        }
        String this$lastHitName = this.getLastHitName();
        String other$lastHitName = other.getLastHitName();
        if (this$lastHitName == null ? other$lastHitName != null : !this$lastHitName.equals(other$lastHitName)) {
            return false;
        }
        String this$lastDamagerName = this.getLastDamagerName();
        String other$lastDamagerName = other.getLastDamagerName();
        if (this$lastDamagerName == null ? other$lastDamagerName != null : !this$lastDamagerName.equals(other$lastDamagerName)) {
            return false;
        }
        BukkitTask this$ninjaTask = this.getNinjaTask();
        BukkitTask other$ninjaTask = other.getNinjaTask();
        return !(this$ninjaTask == null ? other$ninjaTask != null : !this$ninjaTask.equals(other$ninjaTask));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LoadProfile;
    }

    public int hashCode() {
        int PRIME = 59;
        int result2 = 1;
        UUID $uuid = this.getUuid();
        result2 = result2 * 59 + ($uuid == null ? 43 : ((Object)$uuid).hashCode());
        String $lastHitName = this.getLastHitName();
        result2 = result2 * 59 + ($lastHitName == null ? 43 : $lastHitName.hashCode());
        String $lastDamagerName = this.getLastDamagerName();
        result2 = result2 * 59 + ($lastDamagerName == null ? 43 : $lastDamagerName.hashCode());
        BukkitTask $ninjaTask = this.getNinjaTask();
        result2 = result2 * 59 + ($ninjaTask == null ? 43 : $ninjaTask.hashCode());
        return result2;
    }

    public String toString() {
        return "LoadProfile(uuid=" + this.getUuid() + ", lastHitName=" + this.getLastHitName() + ", lastDamagerName=" + this.getLastDamagerName() + ", ninjaTask=" + this.getNinjaTask() + ")";
    }
}

