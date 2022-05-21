/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.tab.TabLayout
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.tab;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.elo.EloHandler;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.tab.PotPvPLayoutProvider;
import com.google.common.collect.Sets;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.tab.TabLayout;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

final class LobbyLayoutProvider
implements BiConsumer<Player, TabLayout> {
    LobbyLayoutProvider() {
    }

    @Override
    public void accept(Player player, TabLayout tabLayout) {
        Party party = PotPvP.getInstance().getPartyHandler().getParty(player);
        EloHandler eloHandler = PotPvP.getInstance().getEloHandler();
        tabLayout.set(1, 3, PotPvP.getInstance().getDominantColor().toString() + ChatColor.BOLD + "Your Rankings");
        int x = 0;
        int y = 4;
        for (KitType kitType : KitType.getAllTypes()) {
            if (kitType.isHidden() || !kitType.isSupportsRanked()) continue;
            tabLayout.set(x++, y, ChatColor.GRAY + kitType.getDisplayName() + " - " + eloHandler.getElo(player, kitType));
            if (x != 3) continue;
            x = 0;
            ++y;
        }
        if (party == null) {
            return;
        }
        tabLayout.set(1, 8, ChatColor.BLUE.toString() + ChatColor.BOLD + "Your Party");
        x = 0;
        y = 9;
        for (UUID member : this.getOrderedMembers(player, party)) {
            int ping = PotPvPLayoutProvider.getPingOrDefault(member);
            String suffix = member == party.getLeader() ? ChatColor.GRAY + "*" : "";
            String displayName = ChatColor.BLUE + FrozenUUIDCache.name((UUID)member) + suffix;
            tabLayout.set(x++, y, displayName, ping);
            if (x == 3 && y == 20) break;
            if (x != 3) continue;
            x = 0;
            ++y;
        }
    }

    private Set<UUID> getOrderedMembers(Player viewer, Party party) {
        Set orderedMembers = Sets.newSetFromMap(new LinkedHashMap());
        UUID leader = party.getLeader();
        orderedMembers.add(viewer.getUniqueId());
        if (viewer.getUniqueId() != leader) {
            orderedMembers.add(leader);
        }
        for (UUID member : party.getMembers()) {
            if (member == leader || member == viewer.getUniqueId()) continue;
            orderedMembers.add(member);
        }
        return orderedMembers;
    }
}

