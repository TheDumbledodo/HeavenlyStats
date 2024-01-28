package dev.dumble.stats.exception;

public class DuplicateEntryException extends RuntimeException {

    public DuplicateEntryException() {
        super("Duplicate entry found. Cannot insert duplicate values.");
    }
}
