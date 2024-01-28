package dev.dumble.stats.mongodb;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CreateCollectionOptions;
import dev.dumble.helper.config.impl.Configuration;
import dev.dumble.stats.HeavenlyStats;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.util.logging.Level;

@UtilityClass
public class MongoManager {

	@Getter @Setter
	private MongoDatabase database;

	public static void startDatabase() {
		MongoClient client = MongoManager.get();
		Configuration configuration = HeavenlyStats.getConfiguration();

		String databaseName = configuration.get("mongo_database.name");
		MongoDatabase database = client.getDatabase(databaseName);

		MongoManager.setDatabase(database);

		Collation collation = Collation.builder()
				.locale("en")
				.caseLevel(false)
				.build();

		CreateCollectionOptions collectionOptions = new CreateCollectionOptions().collation(collation);

		try {
			configuration.getAsMap("mongo_database.collections")
					.values()
                    .forEach(value -> database.createCollection(value, collectionOptions));

		} catch (MongoException exception) {
			HeavenlyStats.getInstance().getLogger().log(Level.SEVERE, "Error whilst trying to initialize collections.", exception);
		}
	}

	public static MongoClient get() {
		return MongoConnection.getInstance().getMongoClient();
	}
}
