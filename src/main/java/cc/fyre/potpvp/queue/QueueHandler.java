/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.Table
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package cc.fyre.potpvp.queue;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.queue.MatchQueue;
import cc.fyre.potpvp.queue.MatchQueueEntry;
import cc.fyre.potpvp.queue.PartyMatchQueueEntry;
import cc.fyre.potpvp.queue.QueueType;
import cc.fyre.potpvp.queue.SoloMatchQueueEntry;
import cc.fyre.potpvp.queue.listener.QueueGeneralListener;
import cc.fyre.potpvp.queue.listener.QueueItemListener;
import cc.fyre.potpvp.util.InventoryUtils;
import cc.fyre.potpvp.validation.PotPvPValidation;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public final class QueueHandler {
    public static final int RANKED_WINDOW_GROWTH_PER_SECOND = 5;
    private static final String JOIN_SOLO_MESSAGE = ChatColor.GREEN + "You are now queued for %s %s" + ChatColor.GREEN + ".";
    private static final String LEAVE_SOLO_MESSAGE = ChatColor.GREEN + "You are no longer queued for %s %s" + ChatColor.GREEN + ".";
    private static final String JOIN_PARTY_MESSAGE = ChatColor.GREEN + "Your party is now queued for %s %s" + ChatColor.GREEN + ".";
    private static final String LEAVE_PARTY_MESSAGE = ChatColor.GREEN + "Your party is no longer queued for %s %s" + ChatColor.GREEN + ".";
    private final Table<KitType, QueueType, MatchQueue> soloQueues = HashBasedTable.create();
    private final Table<KitType, QueueType, MatchQueue> partyQueues = HashBasedTable.create();
    private final Map<UUID, SoloMatchQueueEntry> soloQueueCache = new ConcurrentHashMap<UUID, SoloMatchQueueEntry>();
    private final Map<Party, PartyMatchQueueEntry> partyQueueCache = new ConcurrentHashMap<Party, PartyMatchQueueEntry>();
    private int queuedCount = 0;

    public QueueHandler() {
        Bukkit.getPluginManager().registerEvents((Listener)new QueueGeneralListener(this), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new QueueItemListener(this), (Plugin)PotPvP.getInstance());
        for (KitType kitType : KitType.getAllTypes()) {
            this.soloQueues.put((Object)kitType, (Object)QueueType.PREMIUM, (Object)new MatchQueue(kitType, QueueType.PREMIUM));
            this.soloQueues.put((Object)kitType, (Object)QueueType.RANKED, (Object)new MatchQueue(kitType, QueueType.RANKED));
            this.soloQueues.put((Object)kitType, (Object)QueueType.UNRANKED, (Object)new MatchQueue(kitType, QueueType.UNRANKED));
            this.partyQueues.put((Object)kitType, (Object)QueueType.RANKED, (Object)new MatchQueue(kitType, QueueType.RANKED));
            this.partyQueues.put((Object)kitType, (Object)QueueType.UNRANKED, (Object)new MatchQueue(kitType, QueueType.UNRANKED));
        }
        Bukkit.getScheduler().runTaskTimer((Plugin)PotPvP.getInstance(), () -> {
            this.soloQueues.values().forEach(MatchQueue::tick);
            this.partyQueues.values().forEach(MatchQueue::tick);
            int i = 0;
            for (MatchQueue queue : this.soloQueues.values()) {
                i += queue.countPlayersQueued();
            }
            for (MatchQueue queue : this.partyQueues.values()) {
                i += queue.countPlayersQueued();
            }
            this.queuedCount = i;
        }, 20L, 20L);
    }

    public void addQueues(KitType kitType) {
        this.soloQueues.put((Object)kitType, (Object)QueueType.PREMIUM, (Object)new MatchQueue(kitType, QueueType.PREMIUM));
        this.soloQueues.put((Object)kitType, (Object)QueueType.RANKED, (Object)new MatchQueue(kitType, QueueType.RANKED));
        this.soloQueues.put((Object)kitType, (Object)QueueType.UNRANKED, (Object)new MatchQueue(kitType, QueueType.UNRANKED));
        this.partyQueues.put((Object)kitType, (Object)QueueType.RANKED, (Object)new MatchQueue(kitType, QueueType.RANKED));
        this.partyQueues.put((Object)kitType, (Object)QueueType.UNRANKED, (Object)new MatchQueue(kitType, QueueType.UNRANKED));
    }

    public void removeQueues(KitType kitType) {
        this.soloQueues.remove((Object)kitType, (Object)true);
        this.soloQueues.remove((Object)kitType, (Object)false);
        this.partyQueues.remove((Object)kitType, (Object)true);
        this.partyQueues.remove((Object)kitType, (Object)false);
    }

    public int countPlayersQueued(KitType kitType, QueueType queueType) {
        if (queueType.isPremium()) {
            return ((MatchQueue)this.soloQueues.get((Object)kitType, (Object)queueType)).countPlayersQueued();
        }
        return ((MatchQueue)this.soloQueues.get((Object)kitType, (Object)queueType)).countPlayersQueued() + ((MatchQueue)this.partyQueues.get((Object)kitType, (Object)queueType)).countPlayersQueued();
    }

    public boolean joinQueue(Player player, KitType kitType, QueueType queueType) {
        if (!PotPvPValidation.canJoinQueue(player)) {
            return false;
        }
        MatchQueue queue = (MatchQueue)this.soloQueues.get((Object)kitType, (Object)queueType);
        SoloMatchQueueEntry entry = new SoloMatchQueueEntry(queue, player.getUniqueId());
        if (queueType.isPremium()) {
            PotPvP.getInstance().getPremiumMatchesHandler().removePremiumMatches(player.getUniqueId(), 1);
        }
        queue.addToQueue(entry);
        this.soloQueueCache.put(player.getUniqueId(), entry);
        player.sendMessage(String.format(JOIN_SOLO_MESSAGE, queueType.getName(), kitType.getColoredDisplayName()));
        InventoryUtils.resetInventoryDelayed(player);
        return true;
    }

    public boolean leaveQueue(Player player, boolean selfLeft, boolean silent) {
        SoloMatchQueueEntry entry = this.getQueueEntry(player.getUniqueId());
        if (entry == null) {
            return false;
        }
        MatchQueue queue = entry.getQueue();
        queue.removeFromQueue(entry);
        this.soloQueueCache.remove(player.getUniqueId());
        if (selfLeft && queue.getQueueType().isPremium()) {
            PotPvP.getInstance().getPremiumMatchesHandler().increasePremiumMatches(player.getUniqueId());
        }
        if (!silent) {
            player.sendMessage(String.format(LEAVE_SOLO_MESSAGE, queue.getQueueType().getName(), queue.getKitType().getColoredDisplayName()));
        }
        InventoryUtils.resetInventoryDelayed(player);
        return true;
    }

    public boolean joinQueue(Party party, KitType kitType, boolean ranked) {
        if (!PotPvPValidation.canJoinQueue(party)) {
            return false;
        }
        MatchQueue queue = (MatchQueue)this.partyQueues.get((Object)kitType, (Object)ranked);
        PartyMatchQueueEntry entry = new PartyMatchQueueEntry(queue, party);
        queue.addToQueue(entry);
        this.partyQueueCache.put(party, entry);
        party.message(String.format(JOIN_PARTY_MESSAGE, ranked ? "Ranked" : "Unranked", kitType.getDisplayName()));
        party.resetInventoriesDelayed();
        return true;
    }

    public boolean leaveQueue(Party party, boolean silent) {
        PartyMatchQueueEntry entry = this.getQueueEntry(party);
        if (entry == null) {
            return false;
        }
        MatchQueue queue = entry.getQueue();
        queue.removeFromQueue(entry);
        this.partyQueueCache.remove(party);
        if (!silent) {
            party.message(String.format(LEAVE_PARTY_MESSAGE, queue.getQueueType().getName(), queue.getKitType().getDisplayName()));
        }
        party.resetInventoriesDelayed();
        return true;
    }

    public boolean isQueued(UUID player) {
        return this.soloQueueCache.containsKey(player);
    }

    public boolean isQueuedRanked(UUID player) {
        SoloMatchQueueEntry entry = this.getQueueEntry(player);
        return entry != null && entry.getQueue().getQueueType().isRanked();
    }

    public boolean isQueuedUnranked(UUID player) {
        SoloMatchQueueEntry entry = this.getQueueEntry(player);
        return entry != null && entry.getQueue().getQueueType().isUnranked();
    }

    public boolean isQueuedPremium(UUID player) {
        SoloMatchQueueEntry entry = this.getQueueEntry(player);
        return entry != null && entry.getQueue().getQueueType().isPremium();
    }

    public SoloMatchQueueEntry getQueueEntry(UUID player) {
        return this.soloQueueCache.get(player);
    }

    public boolean isQueued(Party party) {
        return this.partyQueueCache.containsKey(party);
    }

    public boolean isQueuedRanked(Party party) {
        PartyMatchQueueEntry entry = this.getQueueEntry(party);
        return entry != null && entry.getQueue().getQueueType().isRanked();
    }

    public boolean isQueuedUnranked(Party party) {
        PartyMatchQueueEntry entry = this.getQueueEntry(party);
        return entry != null && !entry.getQueue().getQueueType().isRanked();
    }

    public PartyMatchQueueEntry getQueueEntry(Party party) {
        return this.partyQueueCache.get(party);
    }

    void removeFromQueueCache(MatchQueueEntry entry) {
        if (entry instanceof SoloMatchQueueEntry) {
            this.soloQueueCache.remove(((SoloMatchQueueEntry)entry).getPlayer());
        } else if (entry instanceof PartyMatchQueueEntry) {
            this.partyQueueCache.remove(((PartyMatchQueueEntry)entry).getParty());
        }
    }

    public int getQueuedCount() {
        return this.queuedCount;
    }
}

