package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.io.File;

public record PrepackagedPreferencesDatabase(File databaseAssetFile,
                                             SearchablePreferenceScreenGraphProcessor searchablePreferenceScreenGraphProcessor) {
}
