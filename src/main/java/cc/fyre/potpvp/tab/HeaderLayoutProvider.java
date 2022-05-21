/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.tab.TabLayout
 *  rip.bridge.qlib.util.PlayerUtils
 *  rip.bridge.qlib.util.TimeUtils
 */
package cc.fyre.potpvp.tab;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.util.CC;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.BiConsumer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.tab.TabLayout;
import rip.bridge.qlib.util.PlayerUtils;
import rip.bridge.qlib.util.TimeUtils;

final class HeaderLayoutProvider
implements BiConsumer<Player, TabLayout> {
    public static int LAST_IN_FIGHTS_COUNT = 0;
    private long lastUpdated = System.currentTimeMillis();

    HeaderLayoutProvider() {
    }

    @Override
    public void accept(Player player, TabLayout tabLayout) {
        tabLayout.set(1, 1, ChatColor.GRAY + "Your Connection", Math.max((PlayerUtils.getPing((Player)player) + 5) / 10 * 10, 1));
        tabLayout.set(0, 1, ChatColor.GRAY + "Online: " + Bukkit.getOnlinePlayers().size());
        tabLayout.set(2, 1, ChatColor.GRAY + "In Fights: " + LAST_IN_FIGHTS_COUNT);
        if (1000L <= System.currentTimeMillis() - this.lastUpdated) {
            this.lastUpdated = System.currentTimeMillis();
            MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
            LAST_IN_FIGHTS_COUNT = matchHandler.countPlayersPlayingInProgressMatches();
        }
        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
        Date now = Calendar.getInstance(timeZone).getTime();
        String timeStr = new SimpleDateFormat("HH:mm:ss").format(now);
        tabLayout.setHeader(CC.translate("&f" + TimeUtils.formatIntoCalendarStringNoTime((Date)now) + " &7\u2758 &3&lLunar Network &7\u2758 &f" + timeStr));
        tabLayout.setFooter(CC.translate("&fYou are playing &3" + Bukkit.getServerName() + "&f of &blunar.gg"));
    }
}

