package de.KnollFrank.lib.settingssearch.db.preference.db;

import de.KnollFrank.lib.settingssearch.db.preference.db.source.DatabaseSourceProvider;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;

public record PrepackagedPreferencesDatabase<C>(DatabaseSourceProvider databaseSourceProvider,
                                                SearchablePreferenceScreenTreeTransformer<C> searchablePreferenceScreenTreeTransformer) {
}
