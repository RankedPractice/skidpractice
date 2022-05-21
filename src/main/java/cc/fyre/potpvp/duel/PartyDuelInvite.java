/*
 * Decompiled with CFR 0.152.
 */
package cc.fyre.potpvp.duel;

import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.duel.DuelInvite;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.party.Party;

public final class PartyDuelInvite
extends DuelInvite<Party> {
    public PartyDuelInvite(Party sender, Party target, KitType kitTypes, ArenaSchematic arenaSchematic) {
        super(sender, target, kitTypes, arenaSchematic);
    }
}

