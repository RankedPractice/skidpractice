/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.party.menu.otherparties;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.duel.command.DuelCommand;
import cc.fyre.potpvp.party.Party;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

final class OtherPartyButton
extends Button {
    private final Party party;

    OtherPartyButton(Party party) {
        this.party = (Party)Preconditions.checkNotNull((Object)party, (Object)"party");
    }

    public String getName(Player player) {
        return ChatColor.DARK_PURPLE + FrozenUUIDCache.name((UUID)this.party.getLeader());
    }

    public List<String> getDescription(Player player) {
        ArrayList<String> description2 = new ArrayList<String>();
        description2.add("");
        for (UUID member : this.party.getMembers()) {
            ChatColor color = this.party.isLeader(member) ? ChatColor.DARK_PURPLE : ChatColor.YELLOW;
            description2.add(color + FrozenUUIDCache.name((UUID)member));
        }
        description2.add("");
        description2.add(ChatColor.GREEN + "\u00bb Click to duel \u00ab");
        return description2;
    }

    public Material getMaterial(Player player) {
        return Material.SKULL_ITEM;
    }

    public byte getDamageValue(Player player) {
        return 3;
    }

    public int getAmount(Player player) {
        return this.party.getMembers().size();
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        Party senderParty = PotPvP.getInstance().getPartyHandler().getParty(player);
        if (senderParty == null) {
            return;
        }
        if (senderParty.isLeader(player.getUniqueId())) {
            DuelCommand.duel(player, Bukkit.getPlayer((UUID)this.party.getLeader()));
        } else {
            player.sendMessage(ChatColor.RED + "Only the leader can duel other parties!");
        }
    }
}

