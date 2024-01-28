package dev.dumble.stats.command;

import dev.dumble.helper.config.impl.Configuration;
import dev.dumble.stats.HeavenlyStats;
import dev.dumble.stats.util.BukkitHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Leaderboards implements CommandExecutor, TabCompleter {

	private final List<String> leaderboardsArguments = Arrays.stream(LeaderboardsArguments.values())
			.map(argument -> argument.name().toLowerCase())
			.collect(Collectors.toList());

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Configuration configuration = HeavenlyStats.getMessages();
		String message = BukkitHelper.colorize(configuration.get("messages.commands.invalid.argument"));

		if (args.length < 1) {
			configuration.get(message);
			return true;
		}

		try {
			String argument = args[0].toUpperCase();
			LeaderboardsArguments action = LeaderboardsArguments.valueOf(argument);

			action.execute();

		} catch (IllegalArgumentException exception) {
			BukkitHelper.message(sender, message);
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length < 1) return Collections.emptyList();

		ArrayList<String> completions = new ArrayList<>();

		StringUtil.copyPartialMatches(args[0], leaderboardsArguments, completions);
		Collections.sort(completions);

		return completions;
	}
}
