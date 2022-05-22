/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  rip.bridge.bridge.BridgeGlobal
 *  rip.bridge.bridge.global.profile.Profile
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.util;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.setting.Setting;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import rip.bridge.bridge.BridgeGlobal;
import rip.bridge.bridge.global.profile.Profile;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

public class PatchedPlayerUtils {
    public static void resetInventory(Player player) {
        PatchedPlayerUtils.resetInventory(player, null);
    }

    public static void resetInventory(Player player, GameMode gameMode) {
        PatchedPlayerUtils.resetInventory(player, gameMode, KitType.teamFight, false);
    }

    public static void resetInventory(Player player, GameMode gameMode, KitType kitType, boolean skipInvReset) {
        player.setHealth(player.getMaxHealth());
        player.setFallDistance(0.0f);
        player.setFoodLevel(20);
        player.setSaturation(10.0f);
        player.setLevel(0);
        player.setExp(0.0f);
        if (!kitType.getId().equals("Boxing")) {
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }
        }
        if (!skipInvReset) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
        }
        player.setFireTicks(0);
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
        if (gameMode != null && player.getGameMode() != gameMode) {
            player.setGameMode(gameMode);
        }
        if (PotPvP.getInstance().getLobbyHandler().isInLobby(player)) {
            if (player.hasPermission("potpvp.fly") && PotPvP.getInstance().getSettingHandler().getSetting(player, Setting.LOBBY_FLY)) {
                player.setAllowFlight(true);
                player.setFlying(true);
            } else {
                player.setAllowFlight(false);
                player.setFlying(false);
            }
        } else {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }

    public static List<String> mapToNames(Collection<UUID> uuids) {
        return uuids.stream().map(FrozenUUIDCache::name).collect(Collectors.toList());
    }

    public static String getFormattedName(UUID uuid) {
        Profile profile = BridgeGlobal.getProfileHandler().getProfileByUUID(uuid);
        if (profile != null) {
            return profile.getColor() + profile.getUsername();
        }
        return FrozenUUIDCache.name(uuid);
    }
}

