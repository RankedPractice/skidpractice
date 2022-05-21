/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.util.org.apache.commons.lang3.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.ParameterType
 */
package cc.fyre.potpvp.kittype;

import cc.fyre.potpvp.kittype.KitType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.ParameterType;

public final class KitTypeParameterType
implements ParameterType<KitType> {
    public KitType transform(CommandSender sender, String source) {
        for (KitType kitType : KitType.getAllTypes()) {
            if (!sender.isOp() && kitType.isHidden() || !kitType.getId().equalsIgnoreCase(source)) continue;
            return kitType;
        }
        sender.sendMessage(ChatColor.RED + "No kit type with the name " + source + " found.");
        return null;
    }

    public List<String> tabComplete(Player player, Set<String> flags, String source) {
        ArrayList<String> completions = new ArrayList<String>();
        for (KitType kitType : KitType.getAllTypes()) {
            if (!player.isOp() && kitType.isHidden() || !StringUtils.startsWithIgnoreCase((CharSequence)kitType.getId(), (CharSequence)source)) continue;
            completions.add(kitType.getId());
        }
        return completions;
    }
}

