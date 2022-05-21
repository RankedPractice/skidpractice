/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.spigotmc.SpigotConfig
 *  rip.bridge.knockback.Knockback
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 */
package cc.fyre.potpvp.game.command;

import cc.fyre.potpvp.game.event.impl.sumo.SumoGameEvent;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.SpigotConfig;
import rip.bridge.knockback.Knockback;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J$\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\b2\b\b\u0001\u0010\t\u001a\u00020\bH\u0007\u00a8\u0006\n"}, d2={"Lcc/fyre/potpvp/game/command/SetKnockbackProfileCommand;", "", "()V", "execute", "", "sender", "Lorg/bukkit/command/CommandSender;", "eventName", "", "profileName", "potpvp-si"})
public final class SetKnockbackProfileCommand {
    @NotNull
    public static final SetKnockbackProfileCommand INSTANCE = new SetKnockbackProfileCommand();

    private SetKnockbackProfileCommand() {
    }

    @Command(names={"event setkbprofile", "event setkb"}, permission="op", description="Set which KB profile an event uses")
    @JvmStatic
    public static final void execute(@NotNull CommandSender sender, @Param(name="event") @NotNull String eventName, @Param(name="knockbackProfile") @NotNull String profileName) {
        Intrinsics.checkNotNullParameter(sender, "sender");
        Intrinsics.checkNotNullParameter(eventName, "eventName");
        Intrinsics.checkNotNullParameter(profileName, "profileName");
        Knockback profile = SpigotConfig.getKnockbackByName((String)profileName);
        if (profile == null) {
            sender.sendMessage(ChatColor.RED + "A knockback profile by that name does not exist.");
            return;
        }
        String string = eventName;
        boolean bl = false;
        String string2 = string.toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.String).toLowerCase()");
        String string3 = string2;
        if (Intrinsics.areEqual(string3, "sumo")) {
            SumoGameEvent.INSTANCE.setAndSaveKbProfile(profile);
        }
    }
}

