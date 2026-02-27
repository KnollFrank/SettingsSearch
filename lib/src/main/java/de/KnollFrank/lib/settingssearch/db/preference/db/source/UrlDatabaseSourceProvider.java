package de.KnollFrank.lib.settingssearch.db.preference.db.source;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class UrlDatabaseSourceProvider implements DatabaseSourceProvider {

    private final URL url;

    public UrlDatabaseSourceProvider(final URL url) {
        this.url = url;
    }

    @Override
    public InputStream getDatabaseSource() {
        try {
            return url.openStream();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
