package de.KnollFrank.lib.settingssearch.db.preference.db.source;

import java.io.InputStream;

@FunctionalInterface
public interface DatabaseSourceProvider {

    InputStream getDatabaseSource();
}
