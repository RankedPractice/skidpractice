/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.PacketContainer
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  rip.bridge.qlib.qLib
 */
package cc.fyre.potpvp.match.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.nametag.PotPvPNametagProvider;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.SettingHandler;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import rip.bridge.qlib.qLib;

public final class MatchDeathMessageListener
implements Listener {
    private static final String NO_KILLER_MESSAGE = ChatColor.translateAlternateColorCodes((char)'&', (String)"%s&f died.");
    private static final String KILLED_BY_OTHER_MESSAGE = ChatColor.translateAlternateColorCodes((char)'&', (String)"%s&f killed %s&f.");

    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerDeath(PlayerDeathEvent event) {
        SettingHandler settingHandler = PotPvP.getInstance().getSettingHandler();
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchPlaying(event.getEntity());
        if (match == null) {
            return;
        }
        Player killed = event.getEntity();
        Player killer = killed.getKiller();
        PacketContainer lightningPacket = this.createLightningPacket(killed.getLocation());
        float thunderSoundPitch = 0.8f + qLib.RANDOM.nextFloat() * 0.2f;
        float explodeSoundPitch = 0.5f + qLib.RANDOM.nextFloat() * 0.2f;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            UUID onlinePlayerUuid = onlinePlayer.getUniqueId();
            if (match.getTeam(onlinePlayerUuid) == null && !match.isSpectator(onlinePlayerUuid)) continue;
            String killedNameColor = PotPvPNametagProvider.getNameColor(killed, onlinePlayer);
            String killedFormattedName = killedNameColor + killed.getName();
            if (killer == null || match.isSpectator(killer.getUniqueId())) {
                onlinePlayer.sendMessage(String.format(NO_KILLER_MESSAGE, killedFormattedName));
            } else {
                String killerNameColor = PotPvPNametagProvider.getNameColor(killer, onlinePlayer);
                String killerFormattedName = killerNameColor + killer.getName();
                onlinePlayer.sendMessage(String.format(KILLED_BY_OTHER_MESSAGE, killerFormattedName, killedFormattedName));
                for (int i = 0; i < 6; ++i) {
                    PotPvP.getInstance().getKillEffectHandler().playEffect(killer, killed);
                }
            }
            if (!settingHandler.getSetting(onlinePlayer, Setting.VIEW_OTHERS_LIGHTNING)) continue;
            onlinePlayer.playSound(killed.getLocation(), Sound.AMBIENCE_THUNDER, 10000.0f, thunderSoundPitch);
            onlinePlayer.playSound(killed.getLocation(), Sound.EXPLODE, 2.0f, explodeSoundPitch);
            this.sendLightningPacket(onlinePlayer, lightningPacket);
        }
    }

    private PacketContainer createLightningPacket(Location location) {
        PacketContainer lightningPacket = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_WEATHER);
        lightningPacket.getModifier().writeDefaults();
        lightningPacket.getIntegers().write(0, 128);
        lightningPacket.getIntegers().write(4, 1);
        lightningPacket.getIntegers().write(1, ((int)(location.getX() * 32.0)));
        lightningPacket.getIntegers().write(2, ((int)(location.getY() * 32.0)));
        lightningPacket.getIntegers().write(3, ((int)(location.getZ() * 32.0)));
        return lightningPacket;
    }

    private void sendLightningPacket(Player target, PacketContainer packet) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(target, packet);
        }
        catch (InvocationTargetException invocationTargetException) {
            // empty catch block
        }
    }
}

