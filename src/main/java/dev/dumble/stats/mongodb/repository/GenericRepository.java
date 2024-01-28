package dev.dumble.stats.mongodb.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import dev.dumble.stats.model.EntityConverter;
import dev.dumble.stats.mongodb.MongoManager;
import org.bson.Document;

import java.io.Serializable;
import java.util.Optional;

public abstract class GenericRepository<T extends EntityConverter> {

	private final MongoCollection<Document> collection;

	public GenericRepository(String collectionName) {
		this.collection = MongoManager.getDatabase().getCollection(collectionName);
	}

	public abstract String getKeyField();

	public abstract Class<T> getEntityType();

	@SafeVarargs
	public final void create(T... entities) {
		for (T entity : entities) {
			Document document = entity.toDocument();

			collection.createIndex(document);
		}
	}

	@SafeVarargs
	public final void update(T... entities) {
		for (T entity : entities) {
			Serializable keyValue = entity.getKey();

			Document entityDocument = entity.toDocument();
			Document filter = new Document(this.getKeyField(), keyValue);

			collection.replaceOne(filter, entityDocument);
		}
	}

	@SafeVarargs
	public final void delete(T... entities) {
		for (T entity : entities) {
			Serializable keyValue = entity.getKey();
			Document entityDocument = new Document().append(this.getKeyField(), keyValue);

			collection.deleteOne(entityDocument);
		}
	}

	public boolean exists(Serializable serializable) {
		Document query = new Document().append(this.getKeyField(), serializable);

		return collection.countDocuments(query) > 0;
	}

	public Optional<T> retrieve(Serializable serializable) {
		Document entityDocument = new Document().append(this.getKeyField(), serializable);
		FindIterable<T> documents = collection.find(entityDocument, this.getEntityType());

		return Optional.ofNullable(documents.first());
	}
}
