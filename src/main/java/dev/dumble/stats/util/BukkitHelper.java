package dev.dumble.stats.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.regex.Pattern;

@UtilityClass
public class BukkitHelper {

	public final Pattern LEADERBOARD_PATTERN = Pattern.compile("(leaderboards_top_name_|leaderboards_top_value_|)(\\d+)$");

	public String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
}
