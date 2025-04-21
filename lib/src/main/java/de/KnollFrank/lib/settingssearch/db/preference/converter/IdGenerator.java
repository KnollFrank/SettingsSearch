package de.KnollFrank.lib.settingssearch.db.preference.converter;

// FK-TODO: extract interface
public class IdGenerator {

    private int id;

    protected IdGenerator(final int startId) {
        id = startId;
    }

    public int nextId() {
        return id++;
    }
}
