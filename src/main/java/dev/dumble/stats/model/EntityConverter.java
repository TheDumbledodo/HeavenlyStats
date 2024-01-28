package dev.dumble.stats.model;

import org.bson.Document;

import java.io.Serializable;

public interface EntityConverter {

    Serializable getKey();

    Document toDocument();
}