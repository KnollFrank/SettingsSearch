package de.KnollFrank.lib.settingssearch.db.preference.db.source;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

public class UrlDatabaseSourceProvider implements DatabaseSourceProvider {

    private final URL url;

    public UrlDatabaseSourceProvider(final URL url) {
        this.url = url;
    }

    @Override
    public Optional<InputStream> getDatabaseSource() {
        try {
            return Optional.of(url.openStream());
        } catch (final IOException e) {
            return Optional.empty();
        }
    }
}
