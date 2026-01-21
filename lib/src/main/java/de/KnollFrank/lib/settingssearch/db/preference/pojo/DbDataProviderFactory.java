package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Maps;

public class DbDataProviderFactory {

    private DbDataProviderFactory() {
    }

    public static DbDataProvider createDbDataProvider(final SearchablePreferenceScreenTreeEntity.DbDataProvider treeDbDataProvider,
                                                      final SearchablePreferenceScreenEntity.DbDataProvider screenDbDataProvider,
                                                      final SearchablePreferenceEntity.DbDataProvider preferenceDbDataProvider) {
        return new DbDataProvider() {

            @Override
            public Set<SearchablePreferenceScreenEntity> getNodes(final SearchablePreferenceScreenTreeEntity tree) {
                return treeDbDataProvider.getNodes(tree);
            }

            @Override
            public Set<SearchablePreferenceEntity> getAllPreferencesOfPreferenceHierarchy(final SearchablePreferenceScreenEntity screen) {
                return screenDbDataProvider.getAllPreferencesOfPreferenceHierarchy(screen);
            }

            @Override
            public SearchablePreferenceScreenEntity getHost(final SearchablePreferenceEntity preference) {
                return screenDbDataProvider.getHost(preference);
            }

            @Override
            public Set<SearchablePreferenceEntity> getChildren(final SearchablePreferenceEntity preference) {
                return preferenceDbDataProvider.getChildren(preference);
            }

            @Override
            public Optional<SearchablePreferenceEntity> getPredecessor(final SearchablePreferenceEntity preference) {
                return preferenceDbDataProvider.getPredecessor(preference);
            }
        };
    }

    public static DbDataProvider createDbDataProvider(final DbDataProviderData dbDataProviderData) {
        return createDbDataProvider(
                createGraphDbDataProvider(dbDataProviderData),
                createScreenDbDataProvider(dbDataProviderData),
                createPreferenceDataProvider(dbDataProviderData));
    }

    private static SearchablePreferenceScreenTreeEntity.DbDataProvider createGraphDbDataProvider(final DbDataProviderData dbDataProviderData) {
        return new SearchablePreferenceScreenTreeEntity.DbDataProvider() {

            @Override
            public Set<SearchablePreferenceScreenEntity> getNodes(final SearchablePreferenceScreenTreeEntity tree) {
                return Maps
                        .get(dbDataProviderData.nodesByTree(), tree)
                        .orElseThrow();
            }
        };
    }

    private static SearchablePreferenceScreenEntity.DbDataProvider createScreenDbDataProvider(final DbDataProviderData dbDataProviderData) {
        return new SearchablePreferenceScreenEntity.DbDataProvider() {

            @Override
            public Set<SearchablePreferenceEntity> getAllPreferencesOfPreferenceHierarchy(final SearchablePreferenceScreenEntity screen) {
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
        };
    }

    private static SearchablePreferenceEntity.DbDataProvider createPreferenceDataProvider(final DbDataProviderData dbDataProviderData) {
        return new SearchablePreferenceEntity.DbDataProvider() {

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

            @Override
            public SearchablePreferenceScreenEntity getHost(final SearchablePreferenceEntity preference) {
                return Maps
                        .get(dbDataProviderData.hostByPreference(), preference)
                        .orElseThrow();
            }
        };
    }
}
