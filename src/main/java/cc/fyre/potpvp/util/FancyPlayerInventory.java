/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.EntityHuman
 *  net.minecraft.server.v1_7_R4.IInventory
 *  net.minecraft.server.v1_7_R4.ItemStack
 *  net.minecraft.server.v1_7_R4.PlayerInventory
 *  org.bukkit.Bukkit
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftHumanEntity
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventory
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.plugin.Plugin
 */
package cc.fyre.potpvp.util;

import cc.fyre.potpvp.PotPvP;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.IInventory;
import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.PlayerInventory;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class FancyPlayerInventory
extends PlayerInventory {
    private static final Map<UUID, FancyPlayerInventory> storage = new HashMap<UUID, FancyPlayerInventory>();
    private static final Set<UUID> open = new HashSet<UUID>();
    private CraftPlayer owner;
    private boolean playerOnline = false;
    private ItemStack[] extra = new ItemStack[5];
    private CraftInventory inventory = new CraftInventory((IInventory)this);

    private FancyPlayerInventory(Player player) {
        super((EntityHuman)((CraftPlayer)player).getHandle());
        this.owner = (CraftPlayer)player;
        this.playerOnline = player.isOnline();
        this.items = this.player.inventory.items;
        this.armor = this.player.inventory.armor;
        storage.put(this.owner.getUniqueId(), this);
    }

    private Inventory getBukkitInventory() {
        return this.inventory;
    }

    private void removalCheck() {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)PotPvP.getInstance(), () -> this.owner.saveData());
        if (this.transaction.isEmpty() && !this.playerOnline) {
            storage.remove(this.owner.getUniqueId());
        }
    }

    private void onJoin(Player joined) {
        if (!this.playerOnline) {
            CraftPlayer player = (CraftPlayer)joined;
            player.getHandle().inventory.items = this.items;
            player.getHandle().inventory.armor = this.armor;
            this.playerOnline = true;
            Bukkit.getScheduler().runTaskAsynchronously((Plugin)PotPvP.getInstance(), () -> this.owner.saveData());
        }
    }

    private void onQuit() {
        this.playerOnline = false;
        this.removalCheck();
    }

    public void onClose(CraftHumanEntity who) {
        super.onClose(who);
        open.remove(who.getUniqueId());
        this.removalCheck();
    }

    public ItemStack[] getContents() {
        ItemStack[] contents = new ItemStack[this.getSize()];
        System.arraycopy(this.items, 0, contents, 0, this.items.length);
        System.arraycopy(this.items, 0, contents, this.items.length, this.armor.length);
        return contents;
    }

    public int getSize() {
        return super.getSize() + 5;
    }

    public ItemStack getItem(int i) {
        ItemStack[] is;
        if (i >= 0 && i <= 4) {
            i = this.getReversedArmorSlotNum(i);
            is = this.armor;
        } else if (i >= 5 && i <= 8) {
            i -= 4;
            is = this.extra;
        } else {
            i -= 9;
            is = this.items;
            i = this.getReversedItemSlotNum(i);
        }
        if (i >= is.length) {
            i -= is.length;
            is = this.extra;
        } else if (is == this.armor) {
            i = this.getReversedArmorSlotNum(i);
        }
        return is[i];
    }

    public ItemStack splitStack(int i, int j) {
        ItemStack[] is;
        if (i >= 0 && i <= 4) {
            i = this.getReversedArmorSlotNum(i);
            is = this.armor;
        } else if (i >= 5 && i <= 8) {
            i -= 4;
            is = this.extra;
        } else {
            i -= 9;
            is = this.items;
            i = this.getReversedItemSlotNum(i);
        }
        if (i >= is.length) {
            i -= is.length;
            is = this.extra;
        } else if (is == this.armor) {
            i = this.getReversedArmorSlotNum(i);
        }
        if (is[i] != null) {
            if (is[i].count <= j) {
                ItemStack itemstack = is[i];
                is[i] = null;
                return itemstack;
            }
            ItemStack itemstack = is[i].a(j);
            if (is[i].count == 0) {
                is[i] = null;
            }
            return itemstack;
        }
        return null;
    }

    public ItemStack splitWithoutUpdate(int i) {
        ItemStack[] is;
        if (i >= 0 && i <= 4) {
            i = this.getReversedArmorSlotNum(i);
            is = this.armor;
        } else if (i >= 5 && i <= 8) {
            i -= 4;
            is = this.extra;
        } else {
            i -= 9;
            is = this.items;
            i = this.getReversedItemSlotNum(i);
        }
        if (i >= is.length) {
            i -= is.length;
            is = this.extra;
        } else if (is == this.armor) {
            i = this.getReversedArmorSlotNum(i);
        }
        if (is[i] != null) {
            ItemStack itemstack = is[i];
            is[i] = null;
            return itemstack;
        }
        return null;
    }

    public void setItem(int i, ItemStack itemstack) {
        ItemStack[] is;
        if (i >= 0 && i <= 4) {
            i = this.getReversedArmorSlotNum(i);
            is = this.armor;
        } else if (i >= 5 && i <= 8) {
            i -= 4;
            is = this.extra;
        } else {
            i -= 9;
            is = this.items;
            i = this.getReversedItemSlotNum(i);
        }
        if (i >= is.length) {
            i -= is.length;
            is = this.extra;
        } else if (is == this.armor) {
            i = this.getReversedArmorSlotNum(i);
        }
        if (is == this.extra) {
            this.owner.getHandle().drop(itemstack, true);
            itemstack = null;
        }
        is[i] = itemstack;
        this.owner.getHandle().defaultContainer.b();
    }

    private int getReversedItemSlotNum(int i) {
        return i >= 27 ? i - 27 : i + 9;
    }

    private int getReversedArmorSlotNum(int i) {
        switch (i) {
            case 0: {
                return 3;
            }
            case 1: {
                return 2;
            }
            case 2: {
                return 1;
            }
            case 3: {
                return 0;
            }
        }
        return i;
    }

    public String getInventoryName() {
        return "Inventory: " + this.owner.getDisplayName();
    }

    public boolean a(EntityHuman entityhuman) {
        return true;
    }

    public static void open(Player owner, Player openFor) {
        FancyPlayerInventory inventory = storage.containsKey(owner.getUniqueId()) ? storage.get(owner.getUniqueId()) : new FancyPlayerInventory(owner);
        openFor.openInventory(inventory.getBukkitInventory());
        open.add(openFor.getUniqueId());
    }

    public static boolean isViewing(Player player) {
        return open.contains(player.getUniqueId());
    }

    public static void join(Player player) {
        if (storage.containsKey(player.getUniqueId())) {
            storage.get(player.getUniqueId()).onJoin(player);
        }
    }

    public static void quit(Player player) {
        if (storage.containsKey(player.getUniqueId())) {
            storage.get(player.getUniqueId()).onQuit();
        }
    }
}

