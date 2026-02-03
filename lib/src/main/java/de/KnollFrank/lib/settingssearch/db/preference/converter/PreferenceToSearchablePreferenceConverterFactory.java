package de.KnollFrank.lib.settingssearch.db.preference.converter;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.settingssearch.provider.SearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.search.IconProvider;

public class PreferenceToSearchablePreferenceConverterFactory {

    private PreferenceToSearchablePreferenceConverterFactory() {
    }

    public static PreferenceToSearchablePreferenceConverter createPreferenceToSearchablePreferenceConverter(
            final SearchDatabaseConfig<?> searchDatabaseConfig,
            final PreferenceDialogs preferenceDialogs) {
        return new PreferenceToSearchablePreferenceConverter(
                new IconProvider(searchDatabaseConfig.iconResourceIdProvider),
                new SearchableInfoAndDialogInfoProvider(
                        searchDatabaseConfig.searchableInfoProvider,
                        new SearchableDialogInfoOfProvider(
                                preferenceDialogs,
                                searchDatabaseConfig.preferenceDialogAndSearchableInfoProvider)));
    }
}
