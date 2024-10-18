package de.KnollFrank.lib.settingssearch.db.preference.converter;

public class IdGenerator {

    private int id = 1;

    public int nextId() {
        return id++;
    }
}
