package de.KnollFrank.lib.settingssearch.db.preference.converter;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviders;

public class SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverterFactory {

    public static SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter createScreenConverter(final DbDataProviders dbDataProviders) {
        return new SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter(
                entity -> entity.getAllPreferences(dbDataProviders.screenDbDataProvider()),
                new SearchablePreferenceEntityToSearchablePreferenceConverter(
                        dbDataProviders.preferencedbDataProvider()));
    }
}
