package dev.dumble.stats.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.dumble.stats.HeavenlyStats;
import lombok.Getter;

public class MongoConnection {

    @Getter
    private static final MongoConnection instance = new MongoConnection();

    private MongoClient client;

    public MongoClient getMongoClient() {
        if (client != null) return this.client;

        final String uri = HeavenlyStats.getConfiguration().getString("mongo_database.connection_uri");
        final ServerApi serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build();

        final MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(uri))
            .serverApi(serverApi)
            .build();

        this.client = MongoClients.create(settings);

        return this.client;
    }
}
