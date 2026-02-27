package de.KnollFrank.lib.settingssearch.db.preference.db.source;

import java.io.InputStream;
import java.util.Optional;

@FunctionalInterface
public interface DatabaseSourceProvider {

    Optional<InputStream> getDatabaseSource();
}
