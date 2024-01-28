package dev.dumble.stats;

import dev.dumble.stats.mongodb.MongoManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class HeavenlyStats extends JavaPlugin {

	@Getter @Setter
	private static HeavenlyStats instance;

	@Getter @Setter
	private static FileConfiguration configuration;

	@Override
	public void onEnable() {
		HeavenlyStats.setInstance(this);
		HeavenlyStats.setConfiguration(this.getConfig());

		MongoManager.startDatabase();
	}
}