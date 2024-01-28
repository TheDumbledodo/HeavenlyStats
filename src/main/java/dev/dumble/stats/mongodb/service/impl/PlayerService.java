package dev.dumble.stats.mongodb.service.impl;

import dev.dumble.stats.exception.DuplicateEntryException;
import dev.dumble.stats.model.HeavenlyPlayer;
import dev.dumble.stats.mongodb.repository.impl.PlayerRepository;
import dev.dumble.stats.mongodb.service.IGenericService;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Builder(setterPrefix = "set")
public class PlayerService implements IGenericService<HeavenlyPlayer> {

	private final PlayerRepository repository;

	@Getter
	private static final PlayerService instance = PlayerService.builder()
			.setRepository(PlayerRepository.getInstance())
			.build();

	@Override
	public void create(HeavenlyPlayer... players) {
		for (HeavenlyPlayer player : players) {
			UUID uniqueId = player.getUniqueId();

			Optional<HeavenlyPlayer> optionalPlayer = this.retrieve(uniqueId);
			if (optionalPlayer.isPresent()) throw new DuplicateEntryException();

			repository.create(player);
		}
	}

	@Override
	public void update(HeavenlyPlayer... players) {
		repository.update(players);
	}

	@Override
	public void delete(HeavenlyPlayer... players) {
		repository.delete(players);
	}

	@Override
	public boolean exists(UUID uniqueId) {
		if (uniqueId == null)
			throw new IllegalArgumentException("Invalid player uniqueId for retrieval.");

		return repository.exists(uniqueId);
	}

	@Override
	public Optional<HeavenlyPlayer> retrieve(UUID uniqueId) {
		if (uniqueId == null)
			throw new IllegalArgumentException("Invalid player uniqueId for existence check.");

		return repository.retrieve(uniqueId);
	}

	public void resetMobKills() {
		repository.resetMobKills();
	}
}
