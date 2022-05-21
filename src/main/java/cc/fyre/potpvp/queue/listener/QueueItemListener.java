/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.util.Callback
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.queue.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.kittype.menu.select.CustomSelectKitTypeMenu;
import cc.fyre.potpvp.listener.RankedMatchQualificationListener;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.queue.QueueHandler;
import cc.fyre.potpvp.queue.QueueItems;
import cc.fyre.potpvp.queue.QueueType;
import cc.fyre.potpvp.util.ItemListener;
import cc.fyre.potpvp.validation.PotPvPValidation;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.util.Callback;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

public final class QueueItemListener
extends ItemListener {
    private final Function<KitType, CustomSelectKitTypeMenu.CustomKitTypeMeta> selectionAdditionRanked = this.selectionMenuAddition(QueueType.RANKED);
    private final Function<KitType, CustomSelectKitTypeMenu.CustomKitTypeMeta> selectionAdditionUnranked = this.selectionMenuAddition(QueueType.UNRANKED);
    private final Function<KitType, CustomSelectKitTypeMenu.CustomKitTypeMeta> selectionAdditionPremium = this.selectionMenuAddition(QueueType.PREMIUM);
    private final QueueHandler queueHandler;

    public QueueItemListener(QueueHandler queueHandler) {
        this.queueHandler = queueHandler;
        this.addHandler(QueueItems.JOIN_SOLO_UNRANKED_QUEUE_ITEM, this.joinSoloConsumer(QueueType.UNRANKED));
        this.addHandler(QueueItems.JOIN_SOLO_RANKED_QUEUE_ITEM, this.joinSoloConsumer(QueueType.RANKED));
        this.addHandler(QueueItems.JOIN_SOLO_PREMIUM_QUEUE_ITEM, this.joinSoloConsumer(QueueType.PREMIUM));
        this.addHandler(QueueItems.JOIN_PARTY_UNRANKED_QUEUE_ITEM, this.joinPartyConsumer(false));
        this.addHandler(QueueItems.JOIN_PARTY_RANKED_QUEUE_ITEM, this.joinPartyConsumer(true));
        this.addHandler(QueueItems.LEAVE_SOLO_UNRANKED_QUEUE_ITEM, p -> queueHandler.leaveQueue((Player)p, true, false));
        this.addHandler(QueueItems.LEAVE_SOLO_RANKED_QUEUE_ITEM, p -> queueHandler.leaveQueue((Player)p, true, false));
        this.addHandler(QueueItems.LEAVE_SOLO_PREMIUM_QUEUE_ITEM, p -> queueHandler.leaveQueue((Player)p, true, false));
        Consumer<Player> leaveQueuePartyConsumer = player -> {
            Party party = PotPvP.getInstance().getPartyHandler().getParty((Player)player);
            if (party != null && party.isLeader(player.getUniqueId())) {
                queueHandler.leaveQueue(party, false);
            }
        };
        this.addHandler(QueueItems.LEAVE_PARTY_UNRANKED_QUEUE_ITEM, leaveQueuePartyConsumer);
        this.addHandler(QueueItems.LEAVE_PARTY_RANKED_QUEUE_ITEM, leaveQueuePartyConsumer);
    }

    private Consumer<Player> joinSoloConsumer(QueueType queueType) {
        return player -> {
            if ((queueType.isRanked() || queueType.isPremium()) && !RankedMatchQualificationListener.isQualified(player.getUniqueId())) {
                int needed = RankedMatchQualificationListener.getWinsNeededToQualify(player.getUniqueId());
                player.sendMessage(net.md_5.bungee.api.ChatColor.RED + "You can't join " + queueType.getName() + " queues with less than " + 10 + " unranked 1v1 wins. You need " + needed + " more wins!");
                return;
            }
            if (queueType.isPremium() && PotPvP.getInstance().getPremiumMatchesHandler().getPremiumMatches(player.getUniqueId()) <= 0) {
                player.sendMessage(net.md_5.bungee.api.ChatColor.RED + "You can't join premium queues without premium matches!");
                return;
            }
            if (PotPvPValidation.canJoinQueue(player)) {
                new CustomSelectKitTypeMenu((Callback<KitType>)((Callback)kitType -> {
                    this.queueHandler.joinQueue((Player)player, (KitType)kitType, queueType);
                    player.closeInventory();
                }), queueType.isRanked() ? this.selectionAdditionRanked : (queueType.isPremium() ? this.selectionAdditionPremium : this.selectionAdditionUnranked), net.md_5.bungee.api.ChatColor.BLUE + "" + net.md_5.bungee.api.ChatColor.BOLD + "Join " + queueType.getName() + " Queue...", queueType).openMenu((Player)player);
            }
        };
    }

    private Consumer<Player> joinPartyConsumer(boolean ranked) {
        return player -> {
            Party party = PotPvP.getInstance().getPartyHandler().getParty((Player)player);
            if (party == null || !party.isLeader(player.getUniqueId())) {
                return;
            }
            if (ranked) {
                for (UUID member : party.getMembers()) {
                    if (RankedMatchQualificationListener.isQualified(member)) continue;
                    int needed = RankedMatchQualificationListener.getWinsNeededToQualify(member);
                    player.sendMessage(net.md_5.bungee.api.ChatColor.RED + "Your party can't join ranked queues because " + FrozenUUIDCache.name((UUID)member) + " has less than " + 10 + " unranked 1v1 wins. They need " + needed + " more wins!");
                    return;
                }
            }
            if (PotPvPValidation.canJoinQueue(party)) {
                new CustomSelectKitTypeMenu((Callback<KitType>)((Callback)kitType -> {
                    this.queueHandler.joinQueue(party, (KitType)kitType, ranked);
                    player.closeInventory();
                }), ranked ? this.selectionAdditionRanked : this.selectionAdditionUnranked, "Play " + (ranked ? "Ranked" : "Unranked"), ranked ? QueueType.RANKED : QueueType.UNRANKED).openMenu((Player)player);
            }
        };
    }

    private Function<KitType, CustomSelectKitTypeMenu.CustomKitTypeMeta> selectionMenuAddition(QueueType queueType) {
        return kitType -> {
            MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
            int inFightsRanked = matchHandler.countPlayersPlayingMatches(m -> m.getKitType() == kitType && m.getQueueType().isRanked());
            int inQueueRanked = this.queueHandler.countPlayersQueued((KitType)kitType, queueType);
            int inFightsUnranked = matchHandler.countPlayersPlayingMatches(m -> m.getKitType() == kitType && !m.getQueueType().isUnranked());
            int inQueueUnranked = this.queueHandler.countPlayersQueued((KitType)kitType, queueType);
            int inFightsPremium = matchHandler.countPlayersPlayingMatches(m -> m.getKitType() == kitType && !m.getQueueType().isPremium());
            int inQueuePremium = this.queueHandler.countPlayersQueued((KitType)kitType, queueType);
            return new CustomSelectKitTypeMenu.CustomKitTypeMeta(Math.max(1, Math.min(64, queueType.isRanked() ? inQueueRanked + inFightsRanked : (queueType.isPremium() ? inQueuePremium + inFightsPremium : inQueueUnranked + inFightsUnranked))), (List<String>)(queueType.isRanked() ? ImmutableList.of((net.md_5.bungee.api.ChatColor.WHITE + " "), (net.md_5.bungee.api.ChatColor.AQUA + "" + ChatColor.BOLD + net.md_5.bungee.api.ChatColor.UNDERLINE + "Ranked:"), (net.md_5.bungee.api.ChatColor.GREEN + "  In fights: " + net.md_5.bungee.api.ChatColor.WHITE + inFightsRanked), (net.md_5.bungee.api.ChatColor.GREEN + "  In queue: " + net.md_5.bungee.api.ChatColor.WHITE + inQueueRanked), (net.md_5.bungee.api.ChatColor.WHITE + " "), (net.md_5.bungee.api.ChatColor.AQUA + "" + ChatColor.BOLD + "Unranked:"), (net.md_5.bungee.api.ChatColor.GREEN + "  In fights: " + net.md_5.bungee.api.ChatColor.WHITE + inFightsUnranked), (net.md_5.bungee.api.ChatColor.GREEN + "  In queue: " + net.md_5.bungee.api.ChatColor.WHITE + inQueueUnranked)) : (queueType.isPremium() ? ImmutableList.of((net.md_5.bungee.api.ChatColor.WHITE + " "), (net.md_5.bungee.api.ChatColor.RED + "" + ChatColor.BOLD + "Premium:"), (net.md_5.bungee.api.ChatColor.GREEN + "  In fights: " + net.md_5.bungee.api.ChatColor.WHITE + inFightsPremium), (net.md_5.bungee.api.ChatColor.GREEN + "  In queue: " + net.md_5.bungee.api.ChatColor.WHITE + inQueuePremium)) : ImmutableList.of((net.md_5.bungee.api.ChatColor.WHITE + " "), (net.md_5.bungee.api.ChatColor.AQUA + "" + ChatColor.BOLD + "Ranked:"), (net.md_5.bungee.api.ChatColor.GREEN + "  In fights: " + net.md_5.bungee.api.ChatColor.WHITE + inFightsRanked), (net.md_5.bungee.api.ChatColor.GREEN + "  In queue: " + net.md_5.bungee.api.ChatColor.WHITE + inQueueRanked), (net.md_5.bungee.api.ChatColor.AQUA + " "), (net.md_5.bungee.api.ChatColor.AQUA + "" + ChatColor.BOLD + net.md_5.bungee.api.ChatColor.UNDERLINE + "Unranked:"), (net.md_5.bungee.api.ChatColor.GREEN + "  In fights: " + net.md_5.bungee.api.ChatColor.WHITE + inFightsUnranked), (net.md_5.bungee.api.ChatColor.GREEN + "  In queue: " + net.md_5.bungee.api.ChatColor.WHITE + inQueueUnranked)))));
        };
    }
}

