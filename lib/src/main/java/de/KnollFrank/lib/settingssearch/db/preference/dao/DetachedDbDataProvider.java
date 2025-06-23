package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class DetachedDbDataProvider implements DbDataProvider {

    protected final Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> allPreferencesBySearchablePreferenceScreen;
    protected final Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> hostByPreference;
    protected final Map<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>> predecessorByPreference;
    protected final Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> childrenByPreference;

    public DetachedDbDataProvider(final Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> allPreferencesBySearchablePreferenceScreen,
                                  final Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> hostByPreference,
                                  final Map<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>> predecessorByPreference,
                                  final Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> childrenByPreference) {
        this.allPreferencesBySearchablePreferenceScreen = allPreferencesBySearchablePreferenceScreen;
        this.hostByPreference = hostByPreference;
        this.predecessorByPreference = predecessorByPreference;
        this.childrenByPreference = childrenByPreference;
    }

    @Override
    public Set<SearchablePreferenceEntity> getAllPreferences(final SearchablePreferenceScreenEntity screen) {
        return Maps.get(allPreferencesBySearchablePreferenceScreen, screen).orElseThrow();
    }

    @Override
    public SearchablePreferenceScreenEntity getHost(final SearchablePreferenceEntity preference) {
        return Maps.get(hostByPreference, preference).orElseThrow();
    }

    @Override
    public Set<SearchablePreferenceEntity> getChildren(final SearchablePreferenceEntity preference) {
        return Maps.get(childrenByPreference, preference).orElseThrow();
    }

    @Override
    public Optional<SearchablePreferenceEntity> getPredecessor(final SearchablePreferenceEntity preference) {
        return Maps.get(predecessorByPreference, preference).orElseThrow();
    }
}
