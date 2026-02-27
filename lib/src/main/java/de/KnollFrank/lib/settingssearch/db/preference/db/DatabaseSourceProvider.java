package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.io.InputStream;

@FunctionalInterface
public interface DatabaseSourceProvider {

    InputStream getDatabaseSource();
}
