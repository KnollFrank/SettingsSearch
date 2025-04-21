package de.KnollFrank.lib.settingssearch.db.preference.converter;

public class IdGeneratorFactory {

    public static IdGenerator createIdGeneratorStartingAt1() {
        return createIdGeneratorStartingAt(1);
    }

    public static IdGenerator createIdGeneratorStartingAt(final int startId) {
        return new IdGenerator() {

            private int id = startId;

            @Override
            public int nextId() {
                return id++;
            }
        };
    }
}
