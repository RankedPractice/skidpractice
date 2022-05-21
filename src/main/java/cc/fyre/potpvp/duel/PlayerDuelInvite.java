/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package cc.fyre.potpvp.duel;

import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.duel.DuelInvite;
import cc.fyre.potpvp.kittype.KitType;
import java.util.UUID;
import org.bukkit.entity.Player;

public final class PlayerDuelInvite
extends DuelInvite<UUID> {
    public PlayerDuelInvite(Player sender, Player target, KitType kitType, ArenaSchematic arenaSchematic) {
        super(sender.getUniqueId(), target.getUniqueId(), kitType, arenaSchematic);
    }
}

