package de.KnollFrank.lib.settingssearch.db.preference.converter;

// FK-TODO: remove
public class IdGeneratorFactory {

    public static IdGenerator createIdGeneratorStartingAt(final int startId) {
        return new IdGenerator() {

            private int id = startId;

            @Override
            public String nextId() {
                return String.valueOf(id++);
            }
        };
    }
}
