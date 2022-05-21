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
package cc.fyre.potpvp.lobby.menu;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.validation.PotPvPValidation;
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

final class SpectateButton
extends Button {
    private final Match match;

    SpectateButton(Match match) {
        this.match = (Match)Preconditions.checkNotNull((Object)match, (Object)"match");
    }

    public String getName(Player player) {
        return ChatColor.YELLOW.toString() + ChatColor.BOLD + this.match.getSimpleDescription(false);
    }

    public List<String> getDescription(Player player) {
        ArrayList<String> description2 = new ArrayList<String>();
        MatchTeam teamA = this.match.getTeams().get(0);
        MatchTeam teamB = this.match.getTeams().get(1);
        description2.add(ChatColor.GREEN + this.match.getQueueType().getName());
        description2.add("");
        description2.add(ChatColor.YELLOW + "Kit: " + ChatColor.WHITE + this.match.getKitType().getDisplayName());
        description2.add(ChatColor.YELLOW + "Arena: " + ChatColor.WHITE + this.match.getArena().getSchematic());
        ArrayList<UUID> spectators = new ArrayList<UUID>(this.match.getSpectators());
        spectators.removeIf(uuid -> Bukkit.getPlayer((UUID)uuid) != null && Bukkit.getPlayer((UUID)uuid).hasMetadata("ModMode") || this.match.getPreviousTeam((UUID)uuid) != null);
        description2.add(ChatColor.YELLOW + "Spectators: " + ChatColor.WHITE + spectators.size());
        if (teamA.getAliveMembers().size() != 1 || teamB.getAliveMembers().size() != 1) {
            description2.add("");
            for (UUID member : teamA.getAliveMembers()) {
                description2.add(ChatColor.AQUA + FrozenUUIDCache.name((UUID)member));
            }
            description2.add(ChatColor.YELLOW + "   vs.");
            for (UUID member : teamB.getAliveMembers()) {
                description2.add(ChatColor.AQUA + FrozenUUIDCache.name((UUID)member));
            }
        }
        description2.add("");
        description2.add(ChatColor.GREEN + "\u00bb Click to spectate \u00ab");
        return description2;
    }

    public Material getMaterial(Player player) {
        return Material.PAPER;
    }

    public void clicked(Player player, int i, ClickType clickType) {
        if (!PotPvPValidation.canUseSpectateItemIgnoreMatchSpectating(player)) {
            return;
        }
        Match currentlySpectating = PotPvP.getInstance().getMatchHandler().getMatchSpectating(player);
        if (currentlySpectating != null) {
            currentlySpectating.removeSpectator(player, false, true);
        }
        this.match.addSpectator(player, null);
    }
}

