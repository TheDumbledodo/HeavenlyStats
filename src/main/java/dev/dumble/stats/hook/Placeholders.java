package dev.dumble.stats.hook;

import dev.dumble.stats.mongodb.service.impl.PlayerService;
import dev.dumble.stats.util.BukkitHelper;
import lombok.var;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;

public class Placeholders extends PlaceholderExpansion {

	public static final String NO_REPLACE = null;
	public static final String UNKNOWN_PLAYER = "...";

	@Override
	public @NotNull String getIdentifier() {
		return "heavenly";
	}

	@Override
	public @NotNull String getAuthor() {
		return "The_Dumbledodo";
	}

	@Override
	public @NotNull String getVersion() {
		return "0.1";
	}

	@Override
	public @Nullable String onPlaceholderRequest(Player player, @NotNull String parameters) {
		Matcher matcher = BukkitHelper.LEADERBOARD_PATTERN.matcher(parameters);

		if (matcher.find()) {
			var matchedRank = matcher.group(2);
			var rank = Integer.parseInt(matchedRank);

			var optionalPlayer = PlayerService.getInstance().getPlayerByRank(rank);
			if (!optionalPlayer.isPresent()) return UNKNOWN_PLAYER;

			var rankedPlayer = optionalPlayer.get();

			String name = rankedPlayer.getName();
			String mobKills = String.format("%,d", rankedPlayer.getMobKills());

			return parameters.contains("name") ? name : mobKills;
		}
		return NO_REPLACE;
	}
}
