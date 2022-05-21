/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package cc.fyre.potpvp.coinshop.menu.type;

import cc.fyre.potpvp.util.CC;
import org.bukkit.Material;

public enum CoinsShopItem {
    KILLEFFECT_BLOOD(200, 1, Material.REDSTONE, 0, CC.translate("&4&lBlood"), true, "user addperm %player% potpvp.killeffect.blood"),
    KILLEFFECT_PEARL(150, 1, Material.ENDER_PEARL, 0, CC.translate("&a&lPearl"), true, ""),
    KILLEFFECT_FIREWORK(125, 1, Material.FIREWORK, 0, CC.translate("&f&lFireWork"), true, ""),
    KILLEFFECT_WATER(100, 1, Material.WATER_BUCKET, 0, CC.translate("&b&lWater"), true, ""),
    KILLEFFECT_FLAME(100, 1, Material.FLINT_AND_STEEL, 0, CC.translate("&c&lFlame"), true, ""),
    KILLEFFECT_EXPLOSION(100, 1, Material.TNT, 0, CC.translate("&c&lExplosion"), true, ""),
    KILLEFFECT_CLOUD(100, 1, Material.WEB, 0, CC.translate("&b&lCloud"), true, ""),
    KILLEFFECT_SMOKE(100, 1, Material.BONE, 0, CC.translate("&7&lSmoke"), true, "");

    private final int price;
    private final int amount;
    private final Material material;
    private final int data;
    private final String displayName;
    private final boolean useCommand;
    private final String command;

    private CoinsShopItem(int price, int amount, Material material, int data2, String displayName, boolean useCommand, String command) {
        this.price = price;
        this.amount = amount;
        this.material = material;
        this.data = data2;
        this.displayName = displayName;
        this.useCommand = useCommand;
        this.command = command;
    }

    public int getPrice() {
        return this.price;
    }

    public int getAmount() {
        return this.amount;
    }

    public Material getMaterial() {
        return this.material;
    }

    public int getData() {
        return this.data;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public boolean isUseCommand() {
        return this.useCommand;
    }

    public String getCommand() {
        return this.command;
    }
}

