package dev.dumble.stats.mongodb.repository.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.WriteModel;
import dev.dumble.stats.HeavenlyStats;
import dev.dumble.stats.model.HeavenlyPlayer;
import dev.dumble.stats.mongodb.repository.GenericRepository;
import lombok.Getter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class PlayerRepository extends GenericRepository<HeavenlyPlayer> {

	@Getter
	private final static PlayerRepository instance = new PlayerRepository();

	private PlayerRepository() {
		super(HeavenlyStats.getConfiguration().getString("mongo_database.collections.players"));
	}

	@Override
	public Class<HeavenlyPlayer> getEntityType() {
		return HeavenlyPlayer.class;
	}

	@Override
	public String getKeyField() {
		return "uniqueId";
	}

	public void resetMobKills(int fromRank) {
		FindIterable<Document> sortedDocuments = super.getCollection()
				.find()
				.sort(Sorts.descending("mobKills"));

		List<WriteModel<Document>> bulkWrites = new ArrayList<>();

		for (Document document : sortedDocuments) {
			bulkWrites.add(new UpdateOneModel<>(
					new Document("_id", document.getObjectId("_id")),
					new Document("$set", new Document("mobKills", 0))
			));

			if (bulkWrites.size() >= fromRank) break;
		}
		super.getCollection().bulkWrite(bulkWrites);
	}
}
