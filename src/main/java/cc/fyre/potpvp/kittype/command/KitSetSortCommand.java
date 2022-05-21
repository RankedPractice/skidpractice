/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 */
package cc.fyre.potpvp.kittype.command;

import cc.fyre.potpvp.kittype.KitType;
import java.util.Comparator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

public class KitSetSortCommand {
    @Command(names={"kittype setsort"}, permission="op", description="Sets a kit-type's sort")
    public static void execute(Player player, @Param(name="kittype") KitType kitType, @Param(name="sort") int sort) {
        kitType.setSort(sort);
        kitType.saveAsync();
        KitType.allTypes.sort(Comparator.comparing(KitType::getSort));
        player.sendMessage(ChatColor.GREEN + "You've updated this kit-type's sort.");
    }
}

