package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndChildren;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndPredecessor;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class SearchablePreferenceDAOSetter {

    private final SearchablePreference.DbDataProvider dao;

    public SearchablePreferenceDAOSetter(final SearchablePreference.DbDataProvider dao) {
        this.dao = dao;
    }

    public Optional<SearchablePreference> setDao(final Optional<SearchablePreference> searchablePreference) {
        searchablePreference.ifPresent(this::setDao);
        return searchablePreference;
    }

    public Set<PreferenceAndPredecessor> _setDao(final Set<PreferenceAndPredecessor> preferenceAndPredecessors) {
        preferenceAndPredecessors.forEach(this::setDao);
        return preferenceAndPredecessors;
    }

    public Set<PreferenceAndChildren> __setDao(final Set<PreferenceAndChildren> preferenceAndChildren) {
        preferenceAndChildren.forEach(this::setDao);
        return preferenceAndChildren;
    }

    public List<SearchablePreference> setDao(final List<SearchablePreference> searchablePreferences) {
        searchablePreferences.forEach(this::setDao);
        return searchablePreferences;
    }

    public SearchablePreference[] setDao(final SearchablePreference[] searchablePreferences) {
        setDao(Arrays.asList(searchablePreferences));
        return searchablePreferences;
    }

    public Collection<SearchablePreference> setDao(final Collection<SearchablePreference> searchablePreferences) {
        searchablePreferences.forEach(this::setDao);
        return searchablePreferences;
    }

    public Set<SearchablePreference> setDao(final Set<SearchablePreference> searchablePreferences) {
        searchablePreferences.forEach(this::setDao);
        return searchablePreferences;
    }

    public SearchablePreference setDao(final SearchablePreference searchablePreference) {
        searchablePreference.setDao(dao);
        return searchablePreference;
    }

    public void setDao(final Map<SearchablePreference, Set<SearchablePreference>> childrenByPreference) {
        childrenByPreference.forEach(
                (preference, children) -> {
                    setDao(preference);
                    setDao(children);
                });
    }

    public void _setDao(final Map<SearchablePreference, Optional<SearchablePreference>> predecessorByPreference) {
        predecessorByPreference.forEach(
                (preference, predecessor) -> {
                    setDao(preference);
                    setDao(predecessor);
                });
    }

    private PreferenceAndPredecessor setDao(final PreferenceAndPredecessor preferenceAndPredecessor) {
        setDao(preferenceAndPredecessor.getPreference());
        setDao(preferenceAndPredecessor.getPredecessor());
        return preferenceAndPredecessor;
    }

    private PreferenceAndChildren setDao(final PreferenceAndChildren preferenceAndChildren) {
        setDao(preferenceAndChildren.preference());
        setDao(preferenceAndChildren.children());
        return preferenceAndChildren;
    }
}
