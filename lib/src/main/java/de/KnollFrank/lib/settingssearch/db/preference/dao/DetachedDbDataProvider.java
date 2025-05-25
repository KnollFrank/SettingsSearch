package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class DetachedDbDataProvider implements DbDataProvider {

    private final Map<SearchablePreferenceScreen, Set<SearchablePreference>> allPreferencesBySearchablePreferenceScreen;
    private final Map<SearchablePreference, SearchablePreferenceScreen> hostByPreference;
    private final Map<SearchablePreference, Optional<SearchablePreference>> predecessorByPreference;
    private final Map<SearchablePreference, Set<SearchablePreference>> childrenByPreference;

    public DetachedDbDataProvider(final Map<SearchablePreferenceScreen, Set<SearchablePreference>> allPreferencesBySearchablePreferenceScreen,
                                  final Map<SearchablePreference, SearchablePreferenceScreen> hostByPreference,
                                  final Map<SearchablePreference, Optional<SearchablePreference>> predecessorByPreference,
                                  final Map<SearchablePreference, Set<SearchablePreference>> childrenByPreference) {
        this.allPreferencesBySearchablePreferenceScreen = allPreferencesBySearchablePreferenceScreen;
        this.hostByPreference = hostByPreference;
        this.predecessorByPreference = predecessorByPreference;
        this.childrenByPreference = childrenByPreference;
        {
            final SearchablePreferenceDAOSetter searchablePreferenceDAOSetter = new SearchablePreferenceDAOSetter(this);
            final SearchablePreferenceScreenDAOSetter searchablePreferenceScreenDAOSetter =
                    new SearchablePreferenceScreenDAOSetter(
                            this,
                            searchablePreferenceDAOSetter);
            searchablePreferenceScreenDAOSetter._setDao(allPreferencesBySearchablePreferenceScreen);
            searchablePreferenceScreenDAOSetter.setDao(hostByPreference);
            searchablePreferenceDAOSetter._setDao(predecessorByPreference);
            searchablePreferenceDAOSetter.setDao(childrenByPreference);
        }
    }

    @Override
    public Set<SearchablePreference> getAllPreferences(final SearchablePreferenceScreen screen) {
        return Maps.get(allPreferencesBySearchablePreferenceScreen, screen).orElseThrow();
    }

    @Override
    public SearchablePreferenceScreen getHost(final SearchablePreference preference) {
        return Maps.get(hostByPreference, preference).orElseThrow();
    }

    @Override
    public Set<SearchablePreference> getChildren(final SearchablePreference preference) {
        return Maps.get(childrenByPreference, preference).orElseThrow();
    }

    @Override
    public Optional<SearchablePreference> getPredecessor(final SearchablePreference preference) {
        return Maps.get(predecessorByPreference, preference).orElseThrow();
    }
}
