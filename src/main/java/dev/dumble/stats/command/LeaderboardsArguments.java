package dev.dumble.stats.command;

import dev.dumble.stats.HeavenlyStats;
import dev.dumble.stats.mongodb.service.impl.PlayerService;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collection;

public enum LeaderboardsArguments {
	RESET {
		@Override
		public void execute() {
			PlayerService.getInstance().resetAllMobKills();
		}
	},
	RESET_OTHERS {
		@Override
		public void execute() {
			PlayerService.getInstance().resetMobKills(10);
		}
	},
	SPAWN_MOBS {
		@Override
		public void execute() {
			HeavenlyStats instance = HeavenlyStats.getInstance();
			FixedMetadataValue metadata = new FixedMetadataValue(instance, null);

			Collection<? extends Player> players = instance
					.getServer()
					.getOnlinePlayers();

			for (Player onlinePlayer : players) {

				Location location = onlinePlayer.getLocation();
				World world = location.getWorld();

				Zombie zombie = world.spawn(location, Zombie.class);
				zombie.setMetadata("heavenly_mob", metadata);
			}
		}
	};

	public abstract void execute();
}
