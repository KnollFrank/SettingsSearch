package de.KnollFrank.lib.settingssearch.db.preference.db.source;

import java.io.InputStream;
import java.util.Optional;
import java.util.stream.Stream;

public class DatabaseSourceProviders {

    private DatabaseSourceProviders() {
    }

    public static DatabaseSourceProvider firstAvailable(final DatabaseSourceProvider... databaseSourceProviders) {
        return new DatabaseSourceProvider() {

            @Override
            public Optional<InputStream> getDatabaseSource() {
                return Stream.of(databaseSourceProviders)
                        .map(DatabaseSourceProvider::getDatabaseSource)
                        .filter(Optional::isPresent)
                        .map(Optional::orElseThrow)
                        .findFirst();
            }
        };
    }
}
