/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.lunarclient.bukkitapi.LunarClientAPI
 *  com.lunarclient.bukkitapi.object.TitleType
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 */
package cc.fyre.potpvp.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.util.CC;
import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.object.TitleType;
import java.time.Duration;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.replaysystem.replaying.ReplayHelper;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

public final class TestingCommand {
    @Command(names={"testing"}, permission="op")
    public static void silent(Player sender) {
        LunarClientAPI.getInstance().sendTitle(sender, TitleType.TITLE, "Sei un po gay", Duration.ofSeconds(5L));
    }

    @Command(names={"playreplay"}, permission="")
    public static void playReplay(Player p, @Param(name="replay-id") String id) {
        if (PotPvP.getInstance().getMatchHandler().isPlayingOrSpectatingMatch(p)) {
            p.sendMessage(CC.translate("&cYou are currently in a match."));
            return;
        }
        if (ReplaySaver.exists(id) && !ReplayHelper.replaySessions.containsKey(p.getName())) {
            p.sendMessage("\u00a78[\u00a73Replay\u00a78] \u00a7r\u00a77Loading replay \u00a7e" + id + "\u00a77...");
            try {
                ReplaySaver.load(id, replay -> {
                    p.sendMessage("\u00a78[\u00a73Replay\u00a78] \u00a7r\u00a77Replay loaded. Duration \u00a7e" + replay.getData().getDuration() / 20 + "\u00a77 seconds.");
                    replay.play(p);
                });
            }
            catch (Exception e) {
                e.printStackTrace();
                p.sendMessage("\u00a78[\u00a73Replay\u00a78] \u00a7r\u00a77\u00a7cError while loading \u00a7o" + id + ".replay. \u00a7r\u00a7cCheck console for more details.");
            }
        } else {
            p.sendMessage("\u00a78[\u00a73Replay\u00a78] \u00a7r\u00a77\u00a7cReplay not found.");
        }
    }
}

