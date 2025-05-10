package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndChildren;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndPredecessor;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class SearchablePreferenceDAOSetter {

    private final SearchablePreferenceDAO dao;

    public SearchablePreferenceDAOSetter(final SearchablePreferenceDAO dao) {
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

    public List<PreferenceAndChildren> __setDao(final List<PreferenceAndChildren> preferenceAndChildren) {
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

    private SearchablePreference setDao(final SearchablePreference searchablePreference) {
        searchablePreference.setDao(dao);
        return searchablePreference;
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
