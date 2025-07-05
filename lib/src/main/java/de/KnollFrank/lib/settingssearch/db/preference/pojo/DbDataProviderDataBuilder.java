package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DbDataProviderDataBuilder {

    private Map<SearchablePreferenceScreenGraphEntity, Set<SearchablePreferenceScreenEntity>> nodesByGraph = new HashMap<>();
    private Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> allPreferencesBySearchablePreferenceScreen = new HashMap<>();
    private Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> hostByPreference = new HashMap<>();
    private Map<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>> predecessorByPreference = new HashMap<>();
    private Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> childrenByPreference = new HashMap<>();

    protected DbDataProviderDataBuilder() {
    }

    public DbDataProviderDataBuilder withNodesByGraph(final Map<SearchablePreferenceScreenGraphEntity, Set<SearchablePreferenceScreenEntity>> nodesByGraph) {
        this.nodesByGraph = nodesByGraph;
        return this;
    }

    public DbDataProviderDataBuilder withAllPreferencesBySearchablePreferenceScreen(final Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> allPreferencesBySearchablePreferenceScreen) {
        this.allPreferencesBySearchablePreferenceScreen = allPreferencesBySearchablePreferenceScreen;
        return this;
    }

    public DbDataProviderDataBuilder withHostByPreference(final Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> hostByPreference) {
        this.hostByPreference = hostByPreference;
        return this;
    }

    public DbDataProviderDataBuilder withPredecessorByPreference(final Map<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>> predecessorByPreference) {
        this.predecessorByPreference = predecessorByPreference;
        return this;
    }

    public DbDataProviderDataBuilder withChildrenByPreference(final Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> childrenByPreference) {
        this.childrenByPreference = childrenByPreference;
        return this;
    }

    public DbDataProviderData build() {
        return new DbDataProviderData(
                nodesByGraph,
                allPreferencesBySearchablePreferenceScreen,
                hostByPreference,
                predecessorByPreference,
                childrenByPreference);
    }
}