/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.qLib
 */
package cc.fyre.potpvp.game.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.Arena;
import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.game.Game;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.qLib;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lcc/fyre/potpvp/game/command/ForceStartCommand;", "", "()V", "execute", "", "sender", "Lorg/bukkit/entity/Player;", "potpvp-si"})
public final class ForceStartCommand {
    @NotNull
    public static final ForceStartCommand INSTANCE = new ForceStartCommand();

    private ForceStartCommand() {
    }

    @Command(names={"event forcestart"}, permission="op")
    @JvmStatic
    public static final void execute(@NotNull Player sender) {
        Intrinsics.checkNotNullParameter(sender, "sender");
        try {
            Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
            if (game == null) {
                return;
            }
            Game game2 = game;
            ArenaSchematic arenaSchematic = (ArenaSchematic)((Map.Entry)game2.getVoteOptions().entrySet().stream().sorted(ForceStartCommand::execute$lambda-0).limit(1L).collect(Collectors.toList()).get(0)).getKey();
            Optional<Arena> arena = PotPvP.getInstance().getArenaHandler().allocateUnusedArena(arg_0 -> ForceStartCommand.execute$lambda-1(arenaSchematic, arg_0));
            if (arena.isPresent()) {
                Arena arena2 = arena.get();
                Intrinsics.checkNotNullExpressionValue(arena2, "arena.get()");
                game2.setArena(arena2);
                new BukkitRunnable(game2){
                    final /* synthetic */ Game $game;
                    {
                        this.$game = $game;
                    }

                    public void run() {
                        this.$game.start(true);
                    }
                }.runTask((Plugin)qLib.getInstance());
            } else {
                Iterable $this$forEach$iv = game2.getPlayers();
                boolean $i$f$forEach = false;
                for (Object element$iv : $this$forEach$iv) {
                    Player it = (Player)element$iv;
                    boolean bl = false;
                    it.sendMessage(Intrinsics.stringPlus(ChatColor.RED.toString(), "Could not find arena instance of winning map vote."));
                }
            }
        }
        catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Failed to force start event.");
            sender.sendMessage(ChatColor.RED + e.getMessage());
            return;
        }
        sender.sendMessage(ChatColor.GREEN + "Force started event.");
    }

    private static final int execute$lambda-0(Map.Entry o1, Map.Entry o2) {
        return ((AtomicInteger)o1.getValue()).get() - ((AtomicInteger)o2.getValue()).get();
    }

    private static final boolean execute$lambda-1(ArenaSchematic $arenaSchematic, ArenaSchematic it) {
        Intrinsics.checkNotNullParameter($arenaSchematic, "$arenaSchematic");
        return it == $arenaSchematic;
    }
}

