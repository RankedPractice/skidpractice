/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.Potion
 */
package cc.fyre.potpvp.game.event.impl.brackets;

import cc.fyre.potpvp.game.parameter.GameParameter;
import cc.fyre.potpvp.game.parameter.GameParameterOption;
import cc.fyre.potpvp.kittype.KitType;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\u000bB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\b\u001a\u00020\u0004H\u0016J\u000e\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0006H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2={"Lcc/fyre/potpvp/game/event/impl/brackets/BracketsGameKitParameter;", "Lcc/fyre/potpvp/game/parameter/GameParameter;", "()V", "DISPLAY_NAME", "", "options", "", "Lcc/fyre/potpvp/game/event/impl/brackets/BracketsGameKitParameter$BracketsGameKitOption;", "getDisplayName", "getOptions", "Lcc/fyre/potpvp/game/parameter/GameParameterOption;", "BracketsGameKitOption", "potpvp-si"})
public final class BracketsGameKitParameter
implements GameParameter {
    @NotNull
    public static final BracketsGameKitParameter INSTANCE = new BracketsGameKitParameter();
    @NotNull
    private static final String DISPLAY_NAME = "Kit";
    @NotNull
    private static final List<BracketsGameKitOption> options;

    private BracketsGameKitParameter() {
    }

    @Override
    @NotNull
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    @NotNull
    public List<GameParameterOption> getOptions() {
        return options;
    }

    static {
        BracketsGameKitOption[] bracketsGameKitOptionArray = new BracketsGameKitOption[4];
        KitType kitType = KitType.byId("NODEBUFF");
        Intrinsics.checkNotNullExpressionValue(kitType, "byId(\"NODEBUFF\")");
        bracketsGameKitOptionArray[0] = new BracketsGameKitOption(kitType);
        kitType = KitType.byId("SOUP");
        Intrinsics.checkNotNullExpressionValue(kitType, "byId(\"SOUP\")");
        bracketsGameKitOptionArray[1] = new BracketsGameKitOption(kitType);
        kitType = KitType.byId("AXE");
        Intrinsics.checkNotNullExpressionValue(kitType, "byId(\"AXE\")");
        bracketsGameKitOptionArray[2] = new BracketsGameKitOption(kitType);
        kitType = KitType.byId("SG");
        Intrinsics.checkNotNullExpressionValue(kitType, "byId(\"SG\")");
        bracketsGameKitOptionArray[3] = new BracketsGameKitOption(kitType);
        options = CollectionsKt.listOf(bracketsGameKitOptionArray);
    }

    @Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0011\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\u0002\u0010\u000eJ\b\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0011\u001a\u00020\rH\u0016J\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\r0\u0013R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0014"}, d2={"Lcc/fyre/potpvp/game/event/impl/brackets/BracketsGameKitParameter$BracketsGameKitOption;", "Lcc/fyre/potpvp/game/parameter/GameParameterOption;", "kit", "Lcc/fyre/potpvp/kittype/KitType;", "(Lcc/fyre/potpvp/kittype/KitType;)V", "getKit", "()Lcc/fyre/potpvp/kittype/KitType;", "apply", "", "player", "Lorg/bukkit/entity/Player;", "getArmor", "", "Lorg/bukkit/inventory/ItemStack;", "()[Lorg/bukkit/inventory/ItemStack;", "getDisplayName", "", "getIcon", "getItems", "", "potpvp-si"})
    public static final class BracketsGameKitOption
    implements GameParameterOption {
        @NotNull
        private final KitType kit;

        public BracketsGameKitOption(@NotNull KitType kit) {
            Intrinsics.checkNotNullParameter(kit, "kit");
            this.kit = kit;
        }

        @NotNull
        public final KitType getKit() {
            return this.kit;
        }

        @Override
        @NotNull
        public String getDisplayName() {
            String string = this.kit.getDisplayName();
            Intrinsics.checkNotNullExpressionValue(string, "kit.displayName");
            return string;
        }

        @Override
        @NotNull
        public ItemStack getIcon() {
            ItemStack icon = new ItemStack(this.kit.getIcon().getItemType());
            icon.setData(this.kit.getIcon());
            return icon;
        }

        @NotNull
        public final List<ItemStack> getItems() {
            ItemStack[] itemStackArray = this.kit.getDefaultInventory();
            Intrinsics.checkNotNullExpressionValue(itemStackArray, "kit.defaultInventory");
            return ArraysKt.take((Object[])itemStackArray, 9);
        }

        @NotNull
        public final ItemStack[] getArmor() {
            ItemStack[] itemStackArray = this.kit.getDefaultArmor();
            Intrinsics.checkNotNullExpressionValue(itemStackArray, "kit.defaultArmor");
            return itemStackArray;
        }

        public final void apply(@NotNull Player player) {
            Intrinsics.checkNotNullParameter(player, "player");
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.getInventory().setArmorContents(this.getArmor());
            List items = CollectionsKt.toMutableList((Collection)this.getItems());
            ItemStack filler = (ItemStack)items.get(3);
            if (filler != null && filler.getType() != Material.POTION && filler.getType() != Material.MUSHROOM_SOUP) {
                filler = new ItemStack(Material.AIR);
            }
            for (ItemStack item : CollectionsKt.toMutableList(items)) {
                if (item == null) continue;
                if (item.getType() == Material.ENDER_PEARL) {
                    items.remove(item);
                    items.add(filler);
                    continue;
                }
                if (item.getType() == Material.POTION) {
                    Potion potion = Potion.fromItemStack((ItemStack)item);
                    if (potion.isSplash()) continue;
                    potion.apply((LivingEntity)player);
                    items.remove(item);
                    items.add(filler);
                    continue;
                }
                if (item.getType().isEdible() && item.getType() != Material.MUSHROOM_SOUP && item.getType() != Material.GOLDEN_APPLE) {
                    items.remove(item);
                    items.add(filler);
                    continue;
                }
                if (Intrinsics.areEqual(items.get(7), filler) || !Intrinsics.areEqual(items.get(8), filler)) continue;
                items.set(8, item);
                items.set(7, filler);
            }
            Collection $this$toTypedArray$iv = items;
            boolean $i$f$toTypedArray = false;
            Collection thisCollection$iv = $this$toTypedArray$iv;
            ItemStack[] itemStackArray = thisCollection$iv.toArray(new ItemStack[0]);
            if (itemStackArray == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            player.getInventory().setContents(itemStackArray);
            if (player.getInventory().contains(Material.BOW) && !player.getInventory().contains(Material.ARROW)) {
                player.getInventory().setItem(17, new ItemStack(Material.ARROW, 10));
            }
            player.updateInventory();
        }
    }
}

