/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.ComponentBuilder
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 */
package cc.fyre.potpvp.postmatchinv;

import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.util.DisguiseUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public final class PostMatchInvLang {
    static final String LINE = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------------------------------";
    private static final TextComponent COMMA_COMPONENT = new TextComponent(", ");

    static Object[] gen1v1PlayerInvs(UUID winner, UUID loser) {
        return new Object[]{new TextComponent[]{new TextComponent(ChatColor.AQUA + "Inventories: "), PostMatchInvLang.clickToViewLine(winner, true), COMMA_COMPONENT, PostMatchInvLang.clickToViewLine(loser, false)}};
    }

    static Object[] genSpectatorInvs(MatchTeam winner, MatchTeam loser) {
        return new Object[]{new TextComponent(ChatColor.AQUA + "Inventories: "), PostMatchInvLang.clickToViewLine(winner.getAllMembers(), true), PostMatchInvLang.clickToViewLine(loser.getAllMembers(), false)};
    }

    static Object[] genTeamInvs(MatchTeam viewer, MatchTeam winner, MatchTeam loser) {
        return new Object[]{new TextComponent(ChatColor.AQUA + "Inventories: "), PostMatchInvLang.clickToViewLine(winner.getAllMembers(), true), PostMatchInvLang.clickToViewLine(loser.getAllMembers(), false)};
    }

    static Object[] genFfaInvs(MatchTeam winner, Collection<MatchTeam> teams) {
        ArrayList<TextComponent> components = new ArrayList<TextComponent>();
        components.add(new TextComponent(ChatColor.AQUA + "Inventories: "));
        for (UUID uuid : winner.getAllMembers()) {
            components.add(PostMatchInvLang.clickToViewLine(uuid, true));
            components.add(COMMA_COMPONENT);
        }
        for (MatchTeam team : teams) {
            if (team.equals(winner)) continue;
            for (UUID uuid : team.getAllMembers()) {
                components.add(PostMatchInvLang.clickToViewLine(uuid, false));
                components.add(COMMA_COMPONENT);
            }
        }
        components.remove(components.size() - 1);
        return new Object[]{components.toArray(new TextComponent[0])};
    }

    private static TextComponent clickToViewLine(UUID member, boolean winner) {
        String memberName = DisguiseUtil.getDisguisedName(member);
        TextComponent component = new TextComponent();
        component.setText(memberName);
        component.setColor(winner ? ChatColor.GREEN : ChatColor.RED);
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.YELLOW + "Click to view inventory of " + (winner ? ChatColor.GREEN : ChatColor.RED) + memberName).create()));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/_ " + memberName));
        return component;
    }

    private static TextComponent[] clickToViewLine(Set<UUID> members2, boolean winner) {
        ArrayList<TextComponent> components = new ArrayList<TextComponent>();
        for (UUID member : members2) {
            components.add(PostMatchInvLang.clickToViewLine(member, winner));
            components.add(COMMA_COMPONENT);
        }
        components.remove(components.size() - 1);
        return components.toArray(new TextComponent[components.size()]);
    }

    static {
        COMMA_COMPONENT.setColor(ChatColor.GRAY);
    }
}

