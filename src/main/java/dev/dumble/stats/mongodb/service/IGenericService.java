package dev.dumble.stats.mongodb.service;

import java.util.Optional;
import java.util.UUID;

public interface IGenericService<T> {

	void create(T... entities);

	void update(T... entities);

	void delete(T... entities);

	boolean exists(UUID uniqueId);

	Optional<T> retrieve(UUID uniqueId);
}
