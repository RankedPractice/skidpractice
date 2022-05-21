/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  net.minecraft.util.com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.postmatchinv.menu;

import cc.fyre.potpvp.postmatchinv.PostMatchPlayer;
import cc.fyre.potpvp.postmatchinv.menu.PostMatchMenu;
import com.google.common.base.Preconditions;
import java.util.List;
import java.util.UUID;
import net.minecraft.util.com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

final class PostMatchSwapTargetButton
extends Button {
    private final PostMatchPlayer newTarget;

    PostMatchSwapTargetButton(PostMatchPlayer newTarget) {
        this.newTarget = (PostMatchPlayer)Preconditions.checkNotNull((Object)newTarget, (Object)"newTarget");
    }

    public String getName(Player player) {
        return ChatColor.GREEN + "View " + FrozenUUIDCache.name((UUID)this.newTarget.getPlayerUuid()) + "'s inventory";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of((Object)"", (Object)(ChatColor.YELLOW + "Swap your view to " + FrozenUUIDCache.name((UUID)this.newTarget.getPlayerUuid()) + "'s inventory"));
    }

    public Material getMaterial(Player player) {
        return Material.LEVER;
    }

    public void clicked(Player player, int i, ClickType clickType) {
        new PostMatchMenu(this.newTarget).openMenu(player);
    }
}

