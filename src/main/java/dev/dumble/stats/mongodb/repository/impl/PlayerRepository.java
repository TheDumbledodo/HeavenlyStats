package dev.dumble.stats.mongodb.repository.impl;

import dev.dumble.stats.HeavenlyStats;
import dev.dumble.stats.model.HeavenlyPlayer;
import dev.dumble.stats.mongodb.repository.GenericRepository;
import lombok.Getter;

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
}
