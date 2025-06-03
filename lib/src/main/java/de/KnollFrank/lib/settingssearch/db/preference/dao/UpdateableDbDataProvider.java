package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class UpdateableDbDataProvider implements SearchablePreferenceScreenEntity.DbDataProvider, SearchablePreferenceEntity.DbDataProvider {

    public final Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> allPreferencesBySearchablePreferenceScreen = new HashMap<>();
    public final Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> hostByPreference = new HashMap<>();
    public final Map<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>> predecessorByPreference = new HashMap<>();
    public final Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> childrenByPreference = new HashMap<>();

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
