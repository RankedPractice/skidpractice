/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.ChatColor
 */
package cc.fyre.potpvp;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

public final class PotPvPLang {
    public static final String LEFT_ARROW = ChatColor.BLUE.toString() + "\u00bb ";
    public static final String LEFT_ARROW_NAKED = "\u00bb";
    public static final String RIGHT_ARROW = " " + ChatColor.BLUE.toString() + "\u00ab";
    public static final String RIGHT_ARROW_NAKED = "\u00ab";
    public static final String LONG_LINE = ChatColor.STRIKETHROUGH + StringUtils.repeat((String)"-", (int)53);
    public static final String NOT_IN_PARTY = ChatColor.RED + "You are not in a party.";
    public static final String NOT_LEADER_OF_PARTY = ChatColor.RED + "You are not the leader of your party.";
    public static final String ERROR_WHILE_STARTING_MATCH = ChatColor.RED + "There was an error starting the match, please contact an admin.";

    private PotPvPLang() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

