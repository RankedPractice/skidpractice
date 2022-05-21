/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package cc.fyre.potpvp.game.util.team;

import cc.fyre.potpvp.game.parameter.GameParameter;
import cc.fyre.potpvp.game.parameter.GameParameterOption;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0002\t\nB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0004H\u0016J\u000e\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2={"Lcc/fyre/potpvp/game/util/team/GameTeamSizeParameter;", "Lcc/fyre/potpvp/game/parameter/GameParameter;", "()V", "DISPLAY_NAME", "", "getDisplayName", "getOptions", "", "Lcc/fyre/potpvp/game/parameter/GameParameterOption;", "Duos", "Singles", "potpvp-si"})
public final class GameTeamSizeParameter
implements GameParameter {
    @NotNull
    public static final GameTeamSizeParameter INSTANCE = new GameTeamSizeParameter();
    @NotNull
    private static final String DISPLAY_NAME = "Team Size";

    private GameTeamSizeParameter() {
    }

    @Override
    @NotNull
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    @NotNull
    public List<GameParameterOption> getOptions() {
        GameParameterOption[] gameParameterOptionArray = new GameParameterOption[]{Singles.INSTANCE, Duos.INSTANCE};
        return CollectionsKt.listOf(gameParameterOptionArray);
    }

    @Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\u0004H\u0016J\b\u0010\b\u001a\u00020\u0006H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2={"Lcc/fyre/potpvp/game/util/team/GameTeamSizeParameter$Singles;", "Lcc/fyre/potpvp/game/parameter/GameParameterOption;", "()V", "DISPLAY_NAME", "", "icon", "Lorg/bukkit/inventory/ItemStack;", "getDisplayName", "getIcon", "potpvp-si"})
    public static final class Singles
    implements GameParameterOption {
        @NotNull
        public static final Singles INSTANCE = new Singles();
        @NotNull
        private static final String DISPLAY_NAME = "1v1";
        @NotNull
        private static final ItemStack icon = new ItemStack(Material.DIAMOND_HELMET);

        private Singles() {
        }

        @Override
        @NotNull
        public String getDisplayName() {
            return DISPLAY_NAME;
        }

        @Override
        @NotNull
        public ItemStack getIcon() {
            return icon;
        }
    }

    @Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\u0004H\u0016J\b\u0010\b\u001a\u00020\u0006H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2={"Lcc/fyre/potpvp/game/util/team/GameTeamSizeParameter$Duos;", "Lcc/fyre/potpvp/game/parameter/GameParameterOption;", "()V", "DISPLAY_NAME", "", "icon", "Lorg/bukkit/inventory/ItemStack;", "getDisplayName", "getIcon", "potpvp-si"})
    public static final class Duos
    implements GameParameterOption {
        @NotNull
        public static final Duos INSTANCE = new Duos();
        @NotNull
        private static final String DISPLAY_NAME = "2v2";
        @NotNull
        private static final ItemStack icon = new ItemStack(Material.DIAMOND_HELMET, 2);

        private Duos() {
        }

        @Override
        @NotNull
        public String getDisplayName() {
            return DISPLAY_NAME;
        }

        @Override
        @NotNull
        public ItemStack getIcon() {
            return icon;
        }
    }
}

