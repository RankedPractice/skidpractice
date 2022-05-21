/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 */
package cc.fyre.potpvp.postmatchinv.menu;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.HealingMethod;
import cc.fyre.potpvp.postmatchinv.PostMatchInvHandler;
import cc.fyre.potpvp.postmatchinv.PostMatchPlayer;
import cc.fyre.potpvp.postmatchinv.menu.PostMatchFoodLevelButton;
import cc.fyre.potpvp.postmatchinv.menu.PostMatchHealsLeftButton;
import cc.fyre.potpvp.postmatchinv.menu.PostMatchHealthButton;
import cc.fyre.potpvp.postmatchinv.menu.PostMatchPotionEffectsButton;
import cc.fyre.potpvp.postmatchinv.menu.PostMatchStatisticsButton;
import cc.fyre.potpvp.postmatchinv.menu.PostMatchSwapTargetButton;
import cc.fyre.potpvp.util.DisguiseUtil;
import cc.fyre.potpvp.util.InventoryUtils;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;

public final class PostMatchMenu
extends Menu {
    private final PostMatchPlayer target;

    public PostMatchMenu(PostMatchPlayer target) {
        this.target = (PostMatchPlayer)Preconditions.checkNotNull((Object)target, (Object)"target");
    }

    public String getTitle(Player player) {
        return "Inventory of " + DisguiseUtil.getDisguisedName(this.target.getPlayerUuid());
    }

    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        int x = 0;
        int y = 0;
        ArrayList<ItemStack> targetInv = new ArrayList<ItemStack>(Arrays.asList(this.target.getInventory()));
        for (int i = 0; i < 9; ++i) {
            targetInv.add((ItemStack)targetInv.remove(0));
        }
        for (ItemStack inventoryItem : targetInv) {
            buttons.put(this.getSlot(x, y), Button.fromItem((ItemStack)inventoryItem));
            if (x++ <= 7) continue;
            x = 0;
            ++y;
        }
        x = 3;
        for (ItemStack armorItem : this.target.getArmor()) {
            buttons.put(this.getSlot(x--, y), Button.fromItem((ItemStack)armorItem));
        }
        int position = 0;
        buttons.put(this.getSlot(position++, ++y), new PostMatchHealthButton(this.target.getHealth()));
        buttons.put(this.getSlot(position++, y), new PostMatchFoodLevelButton(this.target.getHunger()));
        buttons.put(this.getSlot(position++, y), new PostMatchPotionEffectsButton(this.target.getPotionEffects()));
        HealingMethod healingMethod = this.target.getHealingMethodUsed();
        if (healingMethod != null) {
            int count = healingMethod.count(targetInv.toArray(new ItemStack[targetInv.size()]));
            buttons.put(this.getSlot(position++, y), new PostMatchHealsLeftButton(this.target.getPlayerUuid(), healingMethod, count, this.target.getMissedPots()));
        }
        buttons.put(this.getSlot(position++, y), new PostMatchStatisticsButton(this.target.getTotalHits(), this.target.getLongestCombo()));
        PostMatchInvHandler postMatchInvHandler = PotPvP.getInstance().getPostMatchInvHandler();
        Collection<PostMatchPlayer> postMatchPlayers = postMatchInvHandler.getPostMatchData(player.getUniqueId()).values();
        if (postMatchPlayers.size() == 2) {
            PostMatchPlayer otherPlayer = null;
            for (PostMatchPlayer postMatchPlayer : postMatchPlayers) {
                if (postMatchPlayer.getPlayerUuid().equals(this.target.getPlayerUuid())) continue;
                otherPlayer = postMatchPlayer;
            }
            buttons.put(this.getSlot(8, y), new PostMatchSwapTargetButton(otherPlayer));
        }
        return buttons;
    }

    public void onClose(Player player) {
        InventoryUtils.resetInventoryDelayed(player);
    }
}

