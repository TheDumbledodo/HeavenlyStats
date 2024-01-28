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
import java.util.Optional;
import java.util.UUID;

public class PlayerRepository extends GenericRepository<HeavenlyPlayer> {

	@Getter
	private final static PlayerRepository instance = new PlayerRepository();

	private PlayerRepository() {
		super(HeavenlyStats.getConfiguration().get("mongo_database.collections.players"));
	}

	@Override
	public Class<HeavenlyPlayer> getEntityType() {
		return HeavenlyPlayer.class;
	}

	@Override
	public String getKeyField() {
		return "uniqueId";
	}

	public Optional<HeavenlyPlayer> getPlayerByRank(int rank) {
		FindIterable<Document> sortedDocuments = super.getCollection()
				.find()
				.sort(Sorts.descending("mobKills"));

		FindIterable<Document> documentToRetrieve = sortedDocuments.skip(rank - 1).limit(1);

		for (Document document : documentToRetrieve) {
			String uniqueId = document.getString("uniqueId");

			return Optional.ofNullable(HeavenlyPlayer.builder()
					.setName(document.getString("name"))
					.setUniqueId(UUID.fromString(uniqueId))
					.setMobKills(document.getInteger("mobKills"))
					.build());
		}
		return Optional.empty();
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
