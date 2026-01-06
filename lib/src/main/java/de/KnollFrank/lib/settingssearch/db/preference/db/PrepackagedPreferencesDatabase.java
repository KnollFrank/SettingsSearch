package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.io.File;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenGraphTransformer;

public record PrepackagedPreferencesDatabase<C>(File databaseAssetFile,
                                                SearchablePreferenceScreenGraphTransformer<C> searchablePreferenceScreenGraphTransformer) {
}
