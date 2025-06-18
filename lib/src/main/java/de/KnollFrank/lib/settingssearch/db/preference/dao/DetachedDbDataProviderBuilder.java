package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class DetachedDbDataProviderBuilder {

    private Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> allPreferencesBySearchablePreferenceScreen = new HashMap<>();
    private Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> hostByPreference = new HashMap<>();
    private Map<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>> predecessorByPreference = new HashMap<>();
    private Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> childrenByPreference = new HashMap<>();

    public DetachedDbDataProviderBuilder withAllPreferencesBySearchablePreferenceScreen(final Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> allPreferencesBySearchablePreferenceScreen) {
        this.allPreferencesBySearchablePreferenceScreen = allPreferencesBySearchablePreferenceScreen;
        return this;
    }

    public DetachedDbDataProviderBuilder withHostByPreference(final Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> hostByPreference) {
        this.hostByPreference = hostByPreference;
        return this;
    }

    public DetachedDbDataProviderBuilder withPredecessorByPreference(final Map<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>> predecessorByPreference) {
        this.predecessorByPreference = predecessorByPreference;
        return this;
    }

    public DetachedDbDataProviderBuilder withChildrenByPreference(final Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> childrenByPreference) {
        this.childrenByPreference = childrenByPreference;
        return this;
    }

    public DetachedDbDataProvider createDetachedDbDataProvider() {
        return new DetachedDbDataProvider(
                allPreferencesBySearchablePreferenceScreen,
                hostByPreference,
                predecessorByPreference,
                childrenByPreference);
    }
}