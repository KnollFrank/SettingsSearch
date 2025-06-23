package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Maps;

public class DbDataProviderFactory {

    public static DbDataProvider createDbDataProvider(final DbDataProviderData dbDataProviderData) {
        return new DbDataProvider() {

            @Override
            public Set<SearchablePreferenceEntity> getAllPreferences(final SearchablePreferenceScreenEntity screen) {
                return Maps
                        .get(dbDataProviderData.allPreferencesBySearchablePreferenceScreen(), screen)
                        .orElseThrow();
            }

            @Override
            public SearchablePreferenceScreenEntity getHost(final SearchablePreferenceEntity preference) {
                return Maps
                        .get(dbDataProviderData.hostByPreference(), preference)
                        .orElseThrow();
            }

            @Override
            public Set<SearchablePreferenceEntity> getChildren(final SearchablePreferenceEntity preference) {
                return Maps
                        .get(dbDataProviderData.childrenByPreference(), preference)
                        .orElseThrow();
            }

            @Override
            public Optional<SearchablePreferenceEntity> getPredecessor(final SearchablePreferenceEntity preference) {
                return Maps
                        .get(dbDataProviderData.predecessorByPreference(), preference)
                        .orElseThrow();
            }
        };
    }

    public static DbDataProvider createDbDataProvider(final SearchablePreferenceScreenEntity.DbDataProvider screenDbDataProvider,
                                                      final SearchablePreferenceEntity.DbDataProvider preferencedbDataProvider) {
        return new DbDataProvider() {

            @Override
            public Set<SearchablePreferenceEntity> getAllPreferences(final SearchablePreferenceScreenEntity screen) {
                return screenDbDataProvider.getAllPreferences(screen);
            }

            @Override
            public SearchablePreferenceScreenEntity getHost(final SearchablePreferenceEntity preference) {
                return screenDbDataProvider.getHost(preference);
            }

            @Override
            public Set<SearchablePreferenceEntity> getChildren(final SearchablePreferenceEntity preference) {
                return preferencedbDataProvider.getChildren(preference);
            }

            @Override
            public Optional<SearchablePreferenceEntity> getPredecessor(final SearchablePreferenceEntity preference) {
                return preferencedbDataProvider.getPredecessor(preference);
            }
        };
    }
}
