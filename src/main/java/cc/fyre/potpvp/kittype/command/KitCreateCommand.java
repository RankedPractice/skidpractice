/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.material.MaterialData
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 */
package cc.fyre.potpvp.kittype.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

public class KitCreateCommand {
    @Command(names={"kittype create"}, permission="op", description="Creates a new kit-type")
    public static void execute(Player player, @Param(name="name") String id) {
        if (KitType.byId(id) != null) {
            player.sendMessage(ChatColor.RED + "A kit-type by that name already exists.");
            return;
        }
        KitType kitType = new KitType();
        kitType.id = id;
        kitType.setDisplayName(id);
        kitType.setDisplayColor(ChatColor.GOLD);
        kitType.setIcon(new MaterialData(Material.DIAMOND_SWORD));
        kitType.setSort(50);
        kitType.saveAsync();
        KitType.getAllTypes().add(kitType);
        PotPvP.getInstance().getQueueHandler().addQueues(kitType);
        player.sendMessage(ChatColor.GREEN + "You've created a new kit-type by the ID \"" + kitType.id + "\".");
    }
}

