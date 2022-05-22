/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 */
package cc.fyre.potpvp.match.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kit.Kit;
import cc.fyre.potpvp.kit.KitHandler;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.match.event.MatchCountdownStartEvent;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.pvpclasses.PvPClasses;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class KitSelectionListener
implements Listener {
    @EventHandler
    public void onMatchCountdownStart(MatchCountdownStartEvent event) {
        KitHandler kitHandler = PotPvP.getInstance().getKitHandler();
        Match match = event.getMatch();
        KitType kitType = match.getKitType();
        if (kitType.getId().equals("Sumo")) {
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            MatchTeam team = match.getTeam(player.getUniqueId());
            if (team == null) continue;
            List<Kit> customKits = kitHandler.getKits(player, kitType);
            ItemStack defaultKitItem = Kit.ofDefaultKit(kitType).createSelectionItem();
            if (kitType.equals(KitType.teamFight)) {
                KitType bard = KitType.byId("BARD_HCF");
                KitType diamond = KitType.byId("DIAMOND_HCF");
                KitType archer = KitType.byId("ARCHER_HCF");
                Party party = PotPvP.getInstance().getPartyHandler().getParty(player);
                if (party == null) {
                    Kit.ofDefaultKit(diamond).apply(player);
                } else {
                    PvPClasses kit = party.getKits().getOrDefault(player.getUniqueId(), PvPClasses.DIAMOND);
                    if (kit == null || kit == PvPClasses.DIAMOND) {
                        Kit.ofDefaultKit(diamond).apply(player);
                    } else if (kit == PvPClasses.BARD) {
                        Kit.ofDefaultKit(bard).apply(player);
                    } else {
                        Kit.ofDefaultKit(archer).apply(player);
                    }
                }
            } else if (customKits.isEmpty()) {
                player.getInventory().setItem(0, defaultKitItem);
            } else {
                for (Kit customKit : customKits) {
                    player.getInventory().setItem(customKit.getSlot() - 1, customKit.createSelectionItem());
                }
                player.getInventory().setItem(8, defaultKitItem);
            }
            player.updateInventory();
        }
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchPlaying(event.getPlayer());
        if (match == null) {
            return;
        }
        KitHandler kitHandler = PotPvP.getInstance().getKitHandler();
        ItemStack droppedItem = event.getItemDrop().getItemStack();
        KitType kitType = match.getKitType();
        for (Kit kit : kitHandler.getKits(event.getPlayer(), kitType)) {
            if (!kit.isSelectionItem(droppedItem)) continue;
            event.setCancelled(true);
            return;
        }
        Kit defaultKit = Kit.ofDefaultKit(kitType);
        if (defaultKit.isSelectionItem(droppedItem)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchPlaying(event.getEntity());
        if (match == null) {
            return;
        }
        KitHandler kitHandler = PotPvP.getInstance().getKitHandler();
        KitType kitType = match.getKitType();
        for (Kit kit : kitHandler.getKits(event.getEntity(), kitType)) {
            event.getDrops().remove(kit.createSelectionItem());
        }
        event.getDrops().remove(Kit.ofDefaultKit(kitType).createSelectionItem());
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasItem() || !event.getAction().name().contains("RIGHT_")) {
            return;
        }
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchPlaying(event.getPlayer());
        if (match == null) {
            return;
        }
        KitHandler kitHandler = PotPvP.getInstance().getKitHandler();
        ItemStack clickedItem = event.getItem();
        KitType kitType = match.getKitType();
        Player player = event.getPlayer();
        for (Kit kit : kitHandler.getKits(player, kitType)) {
            if (!kit.isSelectionItem(clickedItem)) continue;
            kit.apply(player);
            player.sendMessage(ChatColor.AQUA + "Successfully loaded kit " + kit.getName() + "!");
            match.getUsedKit().put(player.getUniqueId(), kit);
            return;
        }
        Kit defaultKit = Kit.ofDefaultKit(kitType);
        if (defaultKit.isSelectionItem(clickedItem)) {
            defaultKit.apply(player);
            player.sendMessage(ChatColor.AQUA + "Successfully loaded the default " + kitType.getDisplayName() + " kit!");
            match.getUsedKit().put(player.getUniqueId(), defaultKit);
        }
    }
}

