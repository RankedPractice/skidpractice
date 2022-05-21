/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.Sets
 *  net.md_5.bungee.api.ChatColor
 *  net.md_5.bungee.api.chat.BaseComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 */
package cc.fyre.potpvp.party;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.party.PartyAccessRestriction;
import cc.fyre.potpvp.party.PartyInvite;
import cc.fyre.potpvp.party.PartyLang;
import cc.fyre.potpvp.party.event.PartyCreateEvent;
import cc.fyre.potpvp.party.event.PartyDisbandEvent;
import cc.fyre.potpvp.party.event.PartyMemberJoinEvent;
import cc.fyre.potpvp.party.event.PartyMemberKickEvent;
import cc.fyre.potpvp.party.event.PartyMemberLeaveEvent;
import cc.fyre.potpvp.pvpclasses.PvPClasses;
import cc.fyre.potpvp.util.InventoryUtils;
import cc.fyre.potpvp.util.VisibilityUtils;
import cc.fyre.potpvp.validation.PotPvPValidation;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

public final class Party {
    private final UUID partyId = new UUID(ThreadLocalRandom.current().nextLong(), ThreadLocalRandom.current().nextLong());
    public static final int MAX_SIZE = 30;
    private UUID leader;
    private Map<UUID, PvPClasses> kits = new HashMap<UUID, PvPClasses>();
    private final Set<UUID> members = Sets.newLinkedHashSet();
    private final Set<PartyInvite> invites = Collections.newSetFromMap(new ConcurrentHashMap());
    private PartyAccessRestriction accessRestriction = PartyAccessRestriction.INVITE_ONLY;
    private String password = null;

    Party(UUID leader) {
        this.leader = (UUID)Preconditions.checkNotNull((Object)leader, (Object)"leader");
        this.members.add(leader);
        PotPvP.getInstance().getPartyHandler().updatePartyCache(leader, this);
        Bukkit.getPluginManager().callEvent((Event)new PartyCreateEvent(this));
    }

    public boolean isMember(UUID playerUuid) {
        return this.members.contains(playerUuid);
    }

    public boolean isLeader(UUID playerUuid) {
        return this.leader.equals(playerUuid);
    }

    public Set<UUID> getMembers() {
        return ImmutableSet.copyOf(this.members);
    }

    public Set<PartyInvite> getInvites() {
        return ImmutableSet.copyOf(this.invites);
    }

    public PartyInvite getInvite(UUID target) {
        for (PartyInvite invite : this.invites) {
            if (!invite.getTarget().equals(target)) continue;
            return invite;
        }
        return null;
    }

    public void revokeInvite(PartyInvite invite) {
        this.invites.remove(invite);
    }

    public void invite(Player target) {
        PartyInvite invite = new PartyInvite(this, target.getUniqueId());
        target.spigot().sendMessage((BaseComponent)PartyLang.inviteAcceptPrompt(this));
        this.message(target.getDisplayName() + ChatColor.GOLD + " has been " + ChatColor.GREEN + "invited" + ChatColor.GOLD + " to join your party.");
        this.invites.add(invite);
        Bukkit.getScheduler().runTaskLater((Plugin)PotPvP.getInstance(), () -> this.invites.remove(invite), 600L);
    }

    public void join(Player player) {
        if (this.members.contains(player.getUniqueId())) {
            return;
        }
        if (!PotPvPValidation.canJoinParty(player, this)) {
            return;
        }
        PartyInvite invite = this.getInvite(player.getUniqueId());
        if (invite != null) {
            this.revokeInvite(invite);
        }
        Player leaderBukkit = Bukkit.getPlayer((UUID)this.leader);
        player.sendMessage(ChatColor.GOLD + "You have joined " + leaderBukkit.getDisplayName() + ChatColor.GOLD + "'s party.");
        this.message(player.getDisplayName() + ChatColor.GOLD + " has joined your party.");
        this.members.add(player.getUniqueId());
        PotPvP.getInstance().getPartyHandler().updatePartyCache(player.getUniqueId(), this);
        Bukkit.getPluginManager().callEvent((Event)new PartyMemberJoinEvent(player, this));
        this.forEachOnline(VisibilityUtils::updateVisibility);
        this.resetInventoriesDelayed();
    }

    public void leave(Player player) {
        if (this.isLeader(player.getUniqueId()) && this.members.size() == 1) {
            this.disband();
            return;
        }
        if (!this.members.remove(player.getUniqueId())) {
            return;
        }
        PotPvP.getInstance().getPartyHandler().updatePartyCache(player.getUniqueId(), null);
        if (this.leader.equals(player.getUniqueId())) {
            UUID[] membersArray = this.members.toArray(new UUID[this.members.size()]);
            Player newLeader = Bukkit.getPlayer((UUID)membersArray[ThreadLocalRandom.current().nextInt(membersArray.length)]);
            this.leader = newLeader.getUniqueId();
            this.message(ChatColor.AQUA + newLeader.getName() + ChatColor.YELLOW + " has been randomly promoted to leader of your party.");
        }
        player.sendMessage(ChatColor.YELLOW + "You have left your party.");
        this.message(player.getDisplayName() + ChatColor.GOLD + " has " + ChatColor.RED + "left" + ChatColor.GOLD + " your party.");
        VisibilityUtils.updateVisibility(player);
        this.forEachOnline(VisibilityUtils::updateVisibility);
        Bukkit.getPluginManager().callEvent((Event)new PartyMemberLeaveEvent(player, this));
        InventoryUtils.resetInventoryDelayed(player);
        this.resetInventoriesDelayed();
    }

    public void setLeader(Player player) {
        this.leader = player.getUniqueId();
        this.message(player.getDisplayName() + ChatColor.GOLD + " has been promoted to leader of your party.");
        this.resetInventoriesDelayed();
    }

    public void disband() {
        Bukkit.getPluginManager().callEvent((Event)new PartyDisbandEvent(this));
        PotPvP.getInstance().getPartyHandler().unregisterParty(this);
        this.forEachOnline(player -> {
            VisibilityUtils.updateVisibility(player);
            PotPvP.getInstance().getPartyHandler().updatePartyCache(player.getUniqueId(), null);
        });
        this.message(ChatColor.RED + "Your party has been disbanded.");
        this.resetInventoriesDelayed();
    }

    public void kick(Player player) {
        if (!this.members.remove(player.getUniqueId())) {
            return;
        }
        PotPvP.getInstance().getPartyHandler().updatePartyCache(player.getUniqueId(), null);
        player.sendMessage(ChatColor.GOLD + "You have been kicked from your party.");
        this.message(player.getDisplayName() + ChatColor.GOLD + " has been " + ChatColor.RED + "kicked" + ChatColor.GOLD + " from your party.");
        VisibilityUtils.updateVisibility(player);
        this.forEachOnline(VisibilityUtils::updateVisibility);
        Bukkit.getPluginManager().callEvent((Event)new PartyMemberKickEvent(player, this));
        InventoryUtils.resetInventoryDelayed(player);
        this.resetInventoriesDelayed();
    }

    public void message(String message) {
        this.forEachOnline(p -> p.sendMessage(message));
    }

    public void playSound(Sound sound, float pitch) {
        this.forEachOnline(p -> p.playSound(p.getLocation(), sound, 10.0f, pitch));
    }

    public void resetInventoriesDelayed() {
        Bukkit.getScheduler().runTaskLater((Plugin)PotPvP.getInstance(), this::resetInventoriesNow, 2L);
    }

    public void resetInventoriesNow() {
        this.forEachOnline(InventoryUtils::resetInventoryNow);
    }

    private void forEachOnline(Consumer<Player> consumer) {
        for (UUID member : this.members) {
            Player memberBukkit = Bukkit.getPlayer((UUID)member);
            if (memberBukkit == null) continue;
            consumer.accept(memberBukkit);
        }
    }

    public UUID getPartyId() {
        return this.partyId;
    }

    public UUID getLeader() {
        return this.leader;
    }

    public Map<UUID, PvPClasses> getKits() {
        return this.kits;
    }

    public PartyAccessRestriction getAccessRestriction() {
        return this.accessRestriction;
    }

    public void setAccessRestriction(PartyAccessRestriction accessRestriction) {
        this.accessRestriction = accessRestriction;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

