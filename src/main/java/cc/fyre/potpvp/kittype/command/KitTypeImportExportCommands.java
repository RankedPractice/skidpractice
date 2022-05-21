/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.io.Files
 *  com.google.gson.reflect.TypeToken
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.qLib
 */
package cc.fyre.potpvp.kittype.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.KitType;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.qLib;

public class KitTypeImportExportCommands {
    @Command(names={"kittype export"}, permission="op", async=true)
    public static void executeExport(CommandSender sender) {
        String json = qLib.PLAIN_GSON.toJson(KitType.getAllTypes());
        try {
            Files.write((CharSequence)json, (File)new File(PotPvP.getInstance().getDataFolder(), "kitTypes.json"), (Charset)Charsets.UTF_8);
            sender.sendMessage(ChatColor.GREEN + "Exported.");
        }
        catch (IOException e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Failed to export.");
        }
    }

    @Command(names={"kittype import"}, permission="op", async=true)
    public static void executeImport(CommandSender sender) {
        File file = new File(PotPvP.getInstance().getDataFolder(), "kitTypes.json");
        if (file.exists()) {
            try (BufferedReader schematicsFileReader = Files.newReader((File)file, (Charset)Charsets.UTF_8);){
                Type schematicListType = new TypeToken<List<KitType>>(){}.getType();
                List<KitType> kitTypes = (List)qLib.PLAIN_GSON.fromJson((Reader)schematicsFileReader, schematicListType);
                for (KitType kitType : kitTypes) {
                    KitType.getAllTypes().removeIf(otherKitType -> otherKitType.id.equals(kitType.id));
                    KitType.getAllTypes().add(kitType);
                    kitType.saveAsync();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + "Failed to import.");
            }
        }
        sender.sendMessage(ChatColor.GREEN + "Imported.");
    }
}

