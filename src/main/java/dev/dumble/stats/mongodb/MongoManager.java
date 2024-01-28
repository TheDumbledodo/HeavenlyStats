package dev.dumble.stats.mongodb;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CreateCollectionOptions;
import dev.dumble.stats.HeavenlyStats;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Level;

@UtilityClass
public class MongoManager {

	@Getter @Setter
	private MongoDatabase database;

	public static void startDatabase() {
		MongoClient client = MongoManager.get();
		FileConfiguration configuration = HeavenlyStats.getConfiguration();

		String databaseName = configuration.getString("mongo_database.name");
		MongoDatabase database = client.getDatabase(databaseName);

		MongoManager.setDatabase(database);

		Collation collation = Collation.builder()
				.locale("en")
				.caseLevel(false)
				.build();

		CreateCollectionOptions collectionOptions = new CreateCollectionOptions().collation(collation);

		try {
            ConfigurationSection section = configuration.getConfigurationSection("mongo_database.collections");

			section.getKeys(false)
                    .forEach(key -> database.createCollection(configuration.getString(key), collectionOptions));

		} catch (MongoException exception) {
			HeavenlyStats.getInstance().getLogger().log(Level.SEVERE, "Error whilst trying to initialize collections.", exception);
		}
	}

	public static MongoClient get() {
		return MongoConnection.getInstance().getMongoClient();
	}
}
