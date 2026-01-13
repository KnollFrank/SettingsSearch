package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.io.File;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;

public record PrepackagedPreferencesDatabase<C>(File databaseAssetFile,
                                                SearchablePreferenceScreenTreeTransformer<C> searchablePreferenceScreenTreeTransformer) {
}
