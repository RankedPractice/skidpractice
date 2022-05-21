/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  mkremins.fanciful.FancyMessage
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cc.fyre.potpvp.game.task;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.Arena;
import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.GameState;
import cc.fyre.potpvp.game.bukkit.event.GameStateChangeEvent;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\u0011B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lcc/fyre/potpvp/game/task/GameStartTask;", "", "plugin", "Lorg/bukkit/plugin/java/JavaPlugin;", "game", "Lcc/fyre/potpvp/game/Game;", "(Lorg/bukkit/plugin/java/JavaPlugin;Lcc/fyre/potpvp/game/Game;)V", "interval", "", "startsAt", "", "createHostNotification", "", "Lmkremins/fanciful/FancyMessage;", "eventName", "", "sender", "Task", "potpvp-si"})
public final class GameStartTask {
    @NotNull
    private final JavaPlugin plugin;
    private final long startsAt;
    private final int interval;

    public GameStartTask(@NotNull JavaPlugin plugin, @NotNull Game game) {
        Intrinsics.checkNotNullParameter(plugin, "plugin");
        Intrinsics.checkNotNullParameter(game, "game");
        this.plugin = plugin;
        this.startsAt = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(45L) - (long)5;
        this.interval = 15;
        List<ArenaSchematic> arenas = PotPvP.getInstance().getArenaHandler().findArenaSchematics(arg_0 -> GameStartTask._init_$lambda-0(game, arg_0));
        Intrinsics.checkNotNullExpressionValue(arenas, "arenas");
        Object object = arenas;
        boolean bl = false;
        if (!object.isEmpty()) {
            for (ArenaSchematic arena : arenas) {
                Map<ArenaSchematic, AtomicInteger> map = game.getVoteOptions();
                Intrinsics.checkNotNullExpressionValue(arena, "arena");
                ArenaSchematic arenaSchematic = arena;
                AtomicInteger atomicInteger = new AtomicInteger(0);
                boolean bl2 = false;
                map.put(arenaSchematic, atomicInteger);
            }
            Player player = game.getHost();
            StringBuilder stringBuilder = new StringBuilder().append(ChatColor.GREEN).append("You've started a ");
            object = game.getEvent().getName();
            boolean arena = false;
            Object object2 = object;
            if (object2 == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            String string = ((String)object2).toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string, "(this as java.lang.String).toLowerCase()");
            player.sendMessage(stringBuilder.append(string).append(" event.").toString());
            game.setStartingAt(this.startsAt);
            Bukkit.getPluginManager().callEvent((Event)new GameStateChangeEvent(game, GameState.STARTING));
            new Task(game).runTaskTimer((Plugin)this.plugin, 0L, (long)this.interval * 20L);
        } else {
            Bukkit.getPluginManager().callEvent((Event)new GameStateChangeEvent(game, GameState.ENDED));
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.isOp()) continue;
                player.sendMessage(ChatColor.RED.toString() + "[OPS ONLY] " + game.getHost().getName() + ": Failed to start " + game.getEvent().getName() + " due to lack of an arena.");
            }
        }
    }

    private final List<FancyMessage> createHostNotification(String eventName, String sender) {
        FancyMessage[] fancyMessageArray = new FancyMessage[9];
        FancyMessage fancyMessage = new FancyMessage("\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588").color(ChatColor.GRAY);
        Intrinsics.checkNotNullExpressionValue(fancyMessage, "FancyMessage(\"\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\").color(ChatColor.GRAY)");
        fancyMessageArray[0] = fancyMessage;
        fancyMessage = new FancyMessage("").then("\u2588\u2588").color(ChatColor.GRAY).then("\u2588\u2588\u2588\u2588\u2588\u2588").color(ChatColor.DARK_AQUA).then("\u2588\u2588").color(ChatColor.GRAY);
        Intrinsics.checkNotNullExpressionValue(fancyMessage, "FancyMessage(\"\")\n       \u2026\u2588\").color(ChatColor.GRAY)");
        fancyMessageArray[1] = fancyMessage;
        fancyMessage = new FancyMessage("").then("\u2588\u2588").color(ChatColor.GRAY).then("\u2588").color(ChatColor.DARK_AQUA).then("\u2588\u2588\u2588\u2588\u2588\u2588\u2588").color(ChatColor.GRAY);
        Intrinsics.checkNotNullExpressionValue(fancyMessage, "FancyMessage(\"\")\n       \u2026\u2588\").color(ChatColor.GRAY)");
        fancyMessageArray[2] = fancyMessage;
        ChatColor[] chatColorArray = new ChatColor[]{ChatColor.BOLD};
        fancyMessage = new FancyMessage("").then("\u2588\u2588").color(ChatColor.GRAY).then("\u2588").color(ChatColor.DARK_AQUA).then("\u2588\u2588\u2588\u2588\u2588\u2588\u2588").color(ChatColor.GRAY).then(' ' + eventName + " Event").color(ChatColor.DARK_AQUA).style(chatColorArray);
        Intrinsics.checkNotNullExpressionValue(fancyMessage, "FancyMessage(\"\")\n       \u2026UA).style(ChatColor.BOLD)");
        fancyMessageArray[3] = fancyMessage;
        fancyMessage = new FancyMessage("").then("\u2588\u2588").color(ChatColor.GRAY).then("\u2588\u2588\u2588\u2588\u2588\u2588").color(ChatColor.DARK_AQUA).then("\u2588\u2588").color(ChatColor.GRAY).then(" Hosted by ").color(ChatColor.GRAY).then(sender).color(ChatColor.AQUA);
        Intrinsics.checkNotNullExpressionValue(fancyMessage, "FancyMessage(\"\")\n       \u2026er).color(ChatColor.AQUA)");
        fancyMessageArray[4] = fancyMessage;
        fancyMessage = new FancyMessage("").then("\u2588\u2588\u2588\u2588\u2588\u2588\u2588").color(ChatColor.GRAY).then("\u2588").color(ChatColor.DARK_AQUA).then("\u2588\u2588").color(ChatColor.GRAY).then(" [").color(ChatColor.GRAY).then("Click to join event").color(ChatColor.YELLOW).command("/event join").formattedTooltip(new FancyMessage("Click here to join the event.").color(ChatColor.YELLOW)).then("]").color(ChatColor.GRAY);
        Intrinsics.checkNotNullExpressionValue(fancyMessage, "FancyMessage(\"\")\n       \u2026]\").color(ChatColor.GRAY)");
        fancyMessageArray[5] = fancyMessage;
        fancyMessage = new FancyMessage("").then("\u2588\u2588\u2588\u2588\u2588\u2588\u2588").color(ChatColor.GRAY).then("\u2588").color(ChatColor.DARK_AQUA).then("\u2588\u2588").color(ChatColor.GRAY);
        Intrinsics.checkNotNullExpressionValue(fancyMessage, "FancyMessage(\"\")\n       \u2026\u2588\").color(ChatColor.GRAY)");
        fancyMessageArray[6] = fancyMessage;
        fancyMessage = new FancyMessage("").then("\u2588\u2588").color(ChatColor.GRAY).then("\u2588\u2588\u2588\u2588\u2588\u2588").color(ChatColor.DARK_AQUA).then("\u2588\u2588").color(ChatColor.GRAY);
        Intrinsics.checkNotNullExpressionValue(fancyMessage, "FancyMessage(\"\")\n       \u2026\u2588\").color(ChatColor.GRAY)");
        fancyMessageArray[7] = fancyMessage;
        fancyMessage = new FancyMessage("\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588").color(ChatColor.GRAY);
        Intrinsics.checkNotNullExpressionValue(fancyMessage, "FancyMessage(\"\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\").color(ChatColor.GRAY)");
        fancyMessageArray[8] = fancyMessage;
        return CollectionsKt.arrayListOf(fancyMessageArray);
    }

    private static final boolean _init_$lambda-0(Game $game, ArenaSchematic it) {
        Intrinsics.checkNotNullParameter($game, "$game");
        return Intrinsics.areEqual(it.getEvent(), $game.getEvent()) && it.isEnabled();
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2={"Lcc/fyre/potpvp/game/task/GameStartTask$Task;", "Lorg/bukkit/scheduler/BukkitRunnable;", "game", "Lcc/fyre/potpvp/game/Game;", "(Lcc/fyre/potpvp/game/task/GameStartTask;Lcc/fyre/potpvp/game/Game;)V", "run", "", "potpvp-si"})
    public final class Task
    extends BukkitRunnable {
        @NotNull
        private final Game game;

        public Task(Game game) {
            Intrinsics.checkNotNullParameter(GameStartTask.this, "this$0");
            Intrinsics.checkNotNullParameter(game, "game");
            this.game = game;
        }

        public void run() {
            if (this.game.getState() != GameState.STARTING) {
                this.cancel();
                return;
            }
            if (GameStartTask.this.startsAt <= System.currentTimeMillis() || this.game.getPlayers().size() >= this.game.getMaxPlayers()) {
                ArenaSchematic arenaSchematic = (ArenaSchematic)((Map.Entry)this.game.getVoteOptions().entrySet().stream().sorted(Task::run$lambda-0).limit(1L).collect(Collectors.toList()).get(0)).getKey();
                Optional<Arena> arena = PotPvP.getInstance().getArenaHandler().allocateUnusedArena(arg_0 -> Task.run$lambda-1(arenaSchematic, arg_0));
                if (arena.isPresent()) {
                    Arena arena2 = arena.get();
                    Intrinsics.checkNotNullExpressionValue(arena2, "arena.get()");
                    this.game.setArena(arena2);
                    new BukkitRunnable(this){
                        final /* synthetic */ Task this$0;
                        {
                            this.this$0 = $receiver;
                        }

                        public void run() {
                            Task.access$getGame$p(this.this$0).start(false);
                        }
                    }.runTask((Plugin)GameStartTask.this.plugin);
                } else {
                    Iterable $this$forEach$iv = this.game.getPlayers();
                    boolean $i$f$forEach = false;
                    for (Object element$iv : $this$forEach$iv) {
                        Player it = (Player)element$iv;
                        boolean bl = false;
                        it.sendMessage(Intrinsics.stringPlus(ChatColor.RED.toString(), "Couldn't find an arena instance of winning map vote."));
                    }
                }
                this.cancel();
                return;
            }
            String string = this.game.getEventDisplayName();
            String string2 = this.game.getHost().getDisguisedName();
            Intrinsics.checkNotNullExpressionValue(string2, "game.host.disguisedName");
            List hostNotification = GameStartTask.this.createHostNotification(string, string2);
            for (Player player : Bukkit.getOnlinePlayers()) {
                Iterable $this$forEach$iv = hostNotification;
                boolean $i$f$forEach = false;
                for (Object element$iv : $this$forEach$iv) {
                    FancyMessage it = (FancyMessage)element$iv;
                    boolean bl = false;
                    it.send(player);
                }
            }
        }

        private static final int run$lambda-0(Map.Entry o1, Map.Entry o2) {
            return ((AtomicInteger)o1.getValue()).get() - ((AtomicInteger)o2.getValue()).get();
        }

        private static final boolean run$lambda-1(ArenaSchematic $arenaSchematic, ArenaSchematic it) {
            Intrinsics.checkNotNullParameter($arenaSchematic, "$arenaSchematic");
            return it == $arenaSchematic;
        }

        public static final /* synthetic */ Game access$getGame$p(Task $this) {
            return $this.game;
        }
    }
}

