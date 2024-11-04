package de.KnollFrank.lib.settingssearch.db.preference.converter;

public class StringGenerator {

    private final IdGenerator idGenerator = new IdGenerator();

    public String nextString() {
        return String.valueOf(idGenerator.nextId());
    }
}
