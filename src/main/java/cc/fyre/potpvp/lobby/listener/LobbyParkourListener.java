/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package cc.fyre.potpvp.lobby.listener;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LobbyParkourListener
implements Listener {
    private static Map<UUID, Parkour> parkourMap = Maps.newHashMap();

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=false)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.PHYSICAL) {
            Block block = event.getClickedBlock();
            if (block.getType() == Material.IRON_PLATE) {
                Parkour parkour;
                event.setCancelled(true);
                if (block.getRelative(BlockFace.DOWN).getType() == Material.EMERALD_BLOCK) {
                    Parkour parkour2;
                    if (Parkour.getStartingCheckpoint() == null) {
                        Parkour.setStartingCheckpoint(new Parkour.Checkpoint(block.getLocation()));
                    }
                    if ((parkour2 = parkourMap.get(player.getUniqueId())) == null) {
                        parkour2 = new Parkour();
                        parkour2.getCheckpoint().ring(player);
                        parkourMap.put(player.getUniqueId(), parkour2);
                        player.sendMessage(ChatColor.YELLOW + "You've started the " + ChatColor.GREEN + "parkour" + ChatColor.YELLOW + " challenge!");
                    }
                } else if (block.getRelative(BlockFace.DOWN).getType() == Material.REDSTONE_BLOCK && (parkour = parkourMap.get(player.getUniqueId())) != null) {
                    player.sendMessage(ChatColor.YELLOW + "You've finished the " + ChatColor.GREEN + "parkour" + ChatColor.YELLOW + " challenge!");
                    parkour.getCheckpoint().ring(player);
                    parkourMap.remove(player.getUniqueId());
                }
            } else if (block.getType() == Material.GOLD_PLATE) {
                Parkour.Checkpoint checkpoint;
                event.setCancelled(true);
                Parkour parkour = parkourMap.get(player.getUniqueId());
                if (parkour != null && !(checkpoint = parkour.getCheckpoint()).getLocation().equals((Object)block.getLocation())) {
                    parkour.setCheckpoint(new Parkour.Checkpoint(block.getLocation()));
                    parkour.getCheckpoint().ring(player);
                    player.sendMessage(ChatColor.YELLOW + "You've reached a new" + ChatColor.GOLD + " checkpoint" + ChatColor.YELLOW + "!");
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        parkourMap.remove(event.getPlayer().getUniqueId());
    }

    public static Map<UUID, Parkour> getParkourMap() {
        return parkourMap;
    }

    public static class Parkour {
        private static Checkpoint startingCheckpoint;
        private final long timeStarted = System.currentTimeMillis();
        private Checkpoint checkpoint;

        public Parkour() {
            if (startingCheckpoint != null) {
                this.checkpoint = startingCheckpoint;
            }
        }

        public static Checkpoint getStartingCheckpoint() {
            return startingCheckpoint;
        }

        public static void setStartingCheckpoint(Checkpoint startingCheckpoint) {
            Parkour.startingCheckpoint = startingCheckpoint;
        }

        public long getTimeStarted() {
            return this.timeStarted;
        }

        public Checkpoint getCheckpoint() {
            return this.checkpoint;
        }

        public void setCheckpoint(Checkpoint checkpoint) {
            this.checkpoint = checkpoint;
        }

        public static class Checkpoint {
            private final Location location;

            public Checkpoint(Location location) {
                this.location = location;
            }

            public void ring(Player player) {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 0.0f);
            }

            public Location getLocation() {
                return this.location;
            }
        }
    }
}

