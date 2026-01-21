package de.KnollFrank.lib.settingssearch.db.preference.converter;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider;

public class SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverterFactory {

    private SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverterFactory() {
    }

    public static SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter createScreenConverter(final DbDataProvider dbDataProvider) {
        return new SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter(
                dbDataProvider,
                new SearchablePreferenceEntityToSearchablePreferenceConverter(dbDataProvider));
    }
}
