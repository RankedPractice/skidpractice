/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.plugin.Plugin
 */
package cc.fyre.potpvp.game.tracker;

import cc.fyre.potpvp.PotPvP;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016\u00a8\u0006\u0007"}, d2={"Lcc/fyre/potpvp/game/tracker/ClicksTrackerAdapter;", "Lcom/comphenix/protocol/events/PacketAdapter;", "()V", "onPacketReceiving", "", "event", "Lcom/comphenix/protocol/events/PacketEvent;", "potpvp-si"})
public final class ClicksTrackerAdapter
extends PacketAdapter {
    public ClicksTrackerAdapter() {
        PacketType[] packetTypeArray = new PacketType[]{PacketType.Play.Server.ANIMATION};
        super((Plugin)PotPvP.getInstance(), packetTypeArray);
    }

    public void onPacketReceiving(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Integer n = (Integer)event.getPacket().getIntegers().read(1);
        boolean bl = false;
        if (n == null || n == 0) {
            // empty if block
        }
    }
}

