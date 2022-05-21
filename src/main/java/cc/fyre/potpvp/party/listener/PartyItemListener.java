/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerInteractEvent
 */
package cc.fyre.potpvp.party.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.PartyHandler;
import cc.fyre.potpvp.party.PartyItems;
import cc.fyre.potpvp.party.command.PartyFfaCommand;
import cc.fyre.potpvp.party.command.PartyInfoCommand;
import cc.fyre.potpvp.party.command.PartyLeaveCommand;
import cc.fyre.potpvp.party.command.PartyTeamSplitCommand;
import cc.fyre.potpvp.party.menu.RosterMenu;
import cc.fyre.potpvp.party.menu.otherparties.OtherPartiesMenu;
import cc.fyre.potpvp.util.ItemListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public final class PartyItemListener
extends ItemListener {
    public PartyItemListener(PartyHandler partyHandler) {
        this.addHandler(PartyItems.LEAVE_PARTY_ITEM, PartyLeaveCommand::partyLeave);
        this.addHandler(PartyItems.START_TEAM_SPLIT_ITEM, PartyTeamSplitCommand::partyTeamSplit);
        this.addHandler(PartyItems.START_FFA_ITEM, PartyFfaCommand::partyFfa);
        this.addHandler(PartyItems.OTHER_PARTIES_ITEM, p -> new OtherPartiesMenu().openMenu((Player)p));
        this.addHandler(PartyItems.ASSIGN_CLASSES, p -> new RosterMenu(partyHandler.getParty((Player)p)).openMenu((Player)p));
    }

    @EventHandler
    public void fastPartyIcon(PlayerInteractEvent event) {
        boolean permitted;
        if (!event.hasItem() || !event.getAction().name().contains("RIGHT_")) {
            return;
        }
        if (event.getItem().getType() != PartyItems.ICON_TYPE) {
            return;
        }
        boolean bl = permitted = canUseButton.getOrDefault(event.getPlayer().getUniqueId(), 0L) < System.currentTimeMillis();
        if (permitted) {
            Player player = event.getPlayer();
            Party party = PotPvP.getInstance().getPartyHandler().getParty(player);
            if (party != null && PartyItems.icon(party).isSimilar(event.getItem())) {
                event.setCancelled(true);
                PartyInfoCommand.partyInfo(player, player);
            }
            canUseButton.put(player.getUniqueId(), System.currentTimeMillis() + 500L);
        }
    }
}

