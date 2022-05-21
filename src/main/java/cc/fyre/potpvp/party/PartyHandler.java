/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package cc.fyre.potpvp.party;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.listener.PartyChatListener;
import cc.fyre.potpvp.party.listener.PartyItemListener;
import cc.fyre.potpvp.party.listener.PartyLeaveListener;
import cc.fyre.potpvp.util.InventoryUtils;
import com.google.common.collect.ImmutableSet;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public final class PartyHandler {
    static final int INVITE_EXPIRATION_SECONDS = 30;
    private final Set<Party> parties = Collections.newSetFromMap(new ConcurrentHashMap());
    private final Map<UUID, Party> playerPartyCache = new ConcurrentHashMap<UUID, Party>();

    public PartyHandler() {
        Bukkit.getPluginManager().registerEvents((Listener)new PartyChatListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new PartyItemListener(this), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new PartyLeaveListener(), (Plugin)PotPvP.getInstance());
    }

    public Set<Party> getParties() {
        return ImmutableSet.copyOf(this.parties);
    }

    public boolean hasParty(Player player) {
        return this.playerPartyCache.containsKey(player.getUniqueId());
    }

    public Party getParty(Player player) {
        return this.playerPartyCache.get(player.getUniqueId());
    }

    public Party getParty(UUID uuid) {
        return this.playerPartyCache.get(uuid);
    }

    public Party getOrCreateParty(Player player) {
        Party party = this.getParty(player);
        if (party == null) {
            party = new Party(player.getUniqueId());
            this.parties.add(party);
            InventoryUtils.resetInventoryDelayed(player);
        }
        return party;
    }

    void unregisterParty(Party party) {
        this.parties.remove(party);
    }

    public void updatePartyCache(UUID playerUuid, Party party) {
        if (party != null) {
            this.playerPartyCache.put(playerUuid, party);
        } else {
            this.playerPartyCache.remove(playerUuid);
        }
    }
}

