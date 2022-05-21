/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.ItemStack
 */
package cc.fyre.potpvp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ItemListener
implements Listener {
    protected static final Map<UUID, Long> canUseButton = new ConcurrentHashMap<UUID, Long>();
    private final Map<ItemStack, Consumer<Player>> handlers = new HashMap<ItemStack, Consumer<Player>>();
    private Predicate<Player> preProcessPredicate = null;

    protected final void addHandler(ItemStack stack, Consumer<Player> handler) {
        this.handlers.put(stack, handler);
    }

    protected final void setPreProcessPredicate(Predicate<Player> preProcessPredicate) {
        this.preProcessPredicate = preProcessPredicate;
    }

    public static void addButtonCooldown(Player player, int ms) {
        canUseButton.put(player.getUniqueId(), System.currentTimeMillis() + (long)ms);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasItem() || !event.getAction().name().contains("RIGHT_")) {
            return;
        }
        if (event.getPlayer().hasMetadata("frozen")) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (this.preProcessPredicate != null && !this.preProcessPredicate.test(player)) {
            return;
        }
        for (Map.Entry<ItemStack, Consumer<Player>> entry : this.handlers.entrySet()) {
            boolean permitted;
            if (!item.isSimilar(entry.getKey())) continue;
            boolean bl = permitted = canUseButton.getOrDefault(player.getUniqueId(), 0L) < System.currentTimeMillis();
            if (permitted) {
                entry.getValue().accept(player);
                canUseButton.put(player.getUniqueId(), System.currentTimeMillis() + 500L);
            }
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        canUseButton.remove(event.getPlayer().getUniqueId());
    }
}

