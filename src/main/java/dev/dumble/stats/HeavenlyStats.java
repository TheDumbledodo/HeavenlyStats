package dev.dumble.stats;

import dev.dumble.helper.config.impl.Configuration;
import dev.dumble.helper.config.impl.ConfigurationHelper;
import dev.dumble.stats.command.Leaderboards;
import dev.dumble.stats.hook.Placeholders;
import dev.dumble.stats.listener.BukkitAdapter;
import dev.dumble.stats.mongodb.MongoManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

public class HeavenlyStats extends JavaPlugin {

	@Getter @Setter
	private static HeavenlyStats instance;

	@Getter @Setter
	public static Placeholders placeholders;

	@Getter
	private static final Configuration configuration = Configuration.builder()
			.setResourcesStream(ConfigurationHelper.asResourcesStream("settings/config.yml"))
			.setConfigurationPath("plugins/HeavenlyStats/settings/config.yml")
			.build();

	@Getter
	private static final Configuration messages = Configuration.builder()
			.setResourcesStream(ConfigurationHelper.asResourcesStream("settings/messages.yml"))
			.setConfigurationPath("plugins/HeavenlyStats/settings/messages.yml")
			.build();

	@Override
	public void onEnable() {
		HeavenlyStats.setInstance(this);
		MongoManager.startDatabase();

		this.getServer().getPluginManager().registerEvents(new BukkitAdapter(), this);
		this.getServer().getPluginCommand("leaderboards").setExecutor(new Leaderboards());

		HeavenlyStats.setPlaceholders(new Placeholders());
		HeavenlyStats.getPlaceholders().register();
	}
}