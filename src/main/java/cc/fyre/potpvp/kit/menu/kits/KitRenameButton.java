/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.conversations.Conversable
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.ConversationFactory
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.conversations.StringPrompt
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.kit.menu.kits;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kit.Kit;
import cc.fyre.potpvp.kit.menu.kits.KitsMenu;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.menu.Button;

final class KitRenameButton
extends Button {
    private final Kit kit;

    KitRenameButton(Kit kit) {
        this.kit = (Kit)Preconditions.checkNotNull(kit, "kit");
    }

    public String getName(Player player) {
        return ChatColor.YELLOW.toString() + ChatColor.BOLD + "Rename";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of("", (ChatColor.YELLOW + "Click to rename this kit."));
    }

    public Material getMaterial(Player player) {
        return Material.SIGN;
    }

    public void clicked(final Player player, int slot, ClickType clickType) {
        ConversationFactory factory = new ConversationFactory((Plugin)PotPvP.getInstance()).withFirstPrompt((Prompt)new StringPrompt(){

            public String getPromptText(ConversationContext context) {
                return ChatColor.YELLOW + "Renaming " + ChatColor.RESET + KitRenameButton.this.kit.getName() + ChatColor.GOLD + "... Enter the new name now.";
            }

            public Prompt acceptInput(ConversationContext ctx, String s) {
                if (s.length() > 20) {
                    ctx.getForWhom().sendRawMessage(ChatColor.RED + "Kit names can't have more than 20 characters!");
                    return Prompt.END_OF_CONVERSATION;
                }
                KitRenameButton.this.kit.setName(s);
                PotPvP.getInstance().getKitHandler().saveKitsAsync(player);
                ctx.getForWhom().sendRawMessage(ChatColor.YELLOW + "Kit renamed.");
                if (!PotPvP.getInstance().getMatchHandler().isPlayingMatch(player)) {
                    new KitsMenu(KitRenameButton.this.kit.getType()).openMenu(player);
                }
                return Prompt.END_OF_CONVERSATION;
            }
        }).withLocalEcho(false);
        player.closeInventory();
        player.beginConversation(factory.buildConversation((Conversable)player));
    }
}

