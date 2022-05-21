/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 */
package cc.fyre.potpvp.setting;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public enum Setting {
    SHOW_PLAYERS_IN_LOBBY(ChatColor.LIGHT_PURPLE + "Show Players In Lobby", (List<String>)ImmutableList.of((Object)(ChatColor.BLUE + "If enabled, you will see other"), (Object)(ChatColor.BLUE + "players in the lobby."), (Object)"", (Object)(ChatColor.BLUE + "Disable to only see OPs."), (Object)"", (Object)(ChatColor.BLUE + "This can be overridden at"), (Object)(ChatColor.BLUE + "an admin's discretion.")), Material.JACK_O_LANTERN, ChatColor.YELLOW + "Players are shown", ChatColor.YELLOW + "Players are hidden", true, null),
    SHOW_SCOREBOARD(ChatColor.LIGHT_PURPLE + "Match Scoreboard", (List<String>)ImmutableList.of((Object)(ChatColor.BLUE + "Toggles side scoreboard in-match")), Material.ITEM_FRAME, ChatColor.YELLOW + "Show match scoreboard", ChatColor.YELLOW + "Hide match scoreboard", true, null),
    SHOW_SPECTATOR_JOIN_MESSAGES(ChatColor.AQUA + "Spectator Join Messages", (List<String>)ImmutableList.of((Object)(ChatColor.BLUE + "Enable this to display messages as spectators join.")), Material.BONE, ChatColor.YELLOW + "Show spectator join messages", ChatColor.YELLOW + "Hide spectator join messages", true, null),
    VIEW_OTHER_SPECTATORS(ChatColor.GREEN + "Other Spectators", (List<String>)ImmutableList.of((Object)(ChatColor.BLUE + "If enabled, you can see spectators"), (Object)(ChatColor.BLUE + "in the same match as you."), (Object)"", (Object)(ChatColor.BLUE + "Disable to only see alive players in match.")), Material.GLASS_BOTTLE, ChatColor.YELLOW + "Show other spectators", ChatColor.YELLOW + "Hide other spectators", true, null),
    ALLOW_SPECTATORS(ChatColor.DARK_GREEN + "Allow Spectators", (List<String>)ImmutableList.of((Object)(ChatColor.BLUE + "If enabled, players can spectate your"), (Object)(ChatColor.BLUE + "matches with /spectate."), (Object)"", (Object)(ChatColor.BLUE + "Disable to disallow match spectators.")), Material.REDSTONE_TORCH_ON, ChatColor.YELLOW + "Let players spectate your matches", ChatColor.YELLOW + "Don't let players spectate your matches", true, null),
    RECEIVE_DUELS(ChatColor.GREEN + "Duel Invites", (List<String>)ImmutableList.of((Object)(ChatColor.BLUE + "If enabled, you will be able to receive"), (Object)(ChatColor.BLUE + "duels from other players or parties."), (Object)"", (Object)(ChatColor.BLUE + "Disable to not receive, but still send duels.")), Material.FIRE, ChatColor.YELLOW + "Allow duel invites", ChatColor.YELLOW + "Disallow duel invites", true, "potpvp.toggleduels"),
    VIEW_OTHERS_LIGHTNING(ChatColor.GREEN + "Death Lightning", (List<String>)ImmutableList.of((Object)(ChatColor.BLUE + "If enabled, lightning will be visible"), (Object)(ChatColor.BLUE + "when other players die."), (Object)"", (Object)(ChatColor.BLUE + "Disable to hide others lightning.")), Material.TORCH, ChatColor.YELLOW + "Show other lightning", ChatColor.YELLOW + "Hide other lightning", true, null),
    NIGHT_MODE(ChatColor.GRAY + "Night Mode", (List<String>)ImmutableList.of((Object)(ChatColor.BLUE + "If enabled, your player time will be"), (Object)(ChatColor.BLUE + "changed to night time."), (Object)"", (Object)(ChatColor.BLUE + "Disable to play in day time.")), Material.GLOWSTONE, ChatColor.YELLOW + "Time is set to night", ChatColor.YELLOW + "Time is set to day", false, null),
    ENABLE_GLOBAL_CHAT(ChatColor.RED + "Global Chat", (List<String>)ImmutableList.of((Object)(ChatColor.BLUE + "If enabled, you will see messages"), (Object)(ChatColor.BLUE + "sent in the global chat channel."), (Object)"", (Object)(ChatColor.BLUE + "Disable to only see OP messages.")), Material.BOOK_AND_QUILL, ChatColor.YELLOW + "Global chat is shown", ChatColor.YELLOW + "Global chat is hidden", true, null),
    SEE_TOURNAMENT_JOIN_MESSAGE(ChatColor.DARK_PURPLE + "Tournament Join Messages", (List<String>)ImmutableList.of((Object)(ChatColor.BLUE + "If enabled, you will see messages"), (Object)(ChatColor.BLUE + "when people join the tournament"), (Object)"", (Object)(ChatColor.BLUE + "Disable to only see your own party join messages.")), Material.IRON_DOOR, ChatColor.YELLOW + "Tournament join messages are shown", ChatColor.YELLOW + "Tournament join messages are hidden", true, null),
    SEE_TOURNAMENT_ELIMINATION_MESSAGES(ChatColor.DARK_PURPLE + "Tournament Elimination Messages", (List<String>)ImmutableList.of((Object)(ChatColor.BLUE + "If enabled, you will see messages when"), (Object)(ChatColor.BLUE + "people are eliminated the tournament"), (Object)"", (Object)(ChatColor.BLUE + "Disable to only see your own party elimination messages.")), Material.SKULL_ITEM, ChatColor.YELLOW + "Tournament elimination messages are shown", ChatColor.YELLOW + "Tournament elimination messages are hidden", true, null),
    LOBBY_FLY(ChatColor.DARK_AQUA + "Fly In Lobby", (List<String>)ImmutableList.of((Object)(ChatColor.BLUE + "If enabled, you can double jump"), (Object)(ChatColor.BLUE + "while in the lobby to toggle flight"), (Object)"", (Object)(ChatColor.BLUE + "Disable to turn off flying in the lobby.")), Material.FEATHER, ChatColor.YELLOW + "Flight is enabled", ChatColor.YELLOW + "Flight is disabled", true, "potpvp.fly");

    private final String name;
    private final List<String> description;
    private final Material icon;
    private final String enabledText;
    private final String disabledText;
    private final boolean defaultValue;
    private final String permission;

    public boolean getDefaultValue() {
        return this.defaultValue;
    }

    public boolean canUpdate(Player player) {
        return this.permission == null || player.hasPermission(this.permission);
    }

    private Setting(String name, List<String> description2, Material icon, String enabledText, String disabledText, boolean defaultValue, String permission) {
        this.name = name;
        this.description = description2;
        this.icon = icon;
        this.enabledText = enabledText;
        this.disabledText = disabledText;
        this.defaultValue = defaultValue;
        this.permission = permission;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getDescription() {
        return this.description;
    }

    public Material getIcon() {
        return this.icon;
    }

    public String getEnabledText() {
        return this.enabledText;
    }

    public String getDisabledText() {
        return this.disabledText;
    }
}

