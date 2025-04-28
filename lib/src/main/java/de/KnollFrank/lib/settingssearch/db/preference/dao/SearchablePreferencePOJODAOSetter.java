package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndChildren;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndPredecessor;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class SearchablePreferencePOJODAOSetter {

    private final SearchablePreferencePOJODAO dao;

    public SearchablePreferencePOJODAOSetter(final SearchablePreferencePOJODAO dao) {
        this.dao = dao;
    }

    public Optional<SearchablePreferencePOJO> setDao(final Optional<SearchablePreferencePOJO> searchablePreferencePOJO) {
        searchablePreferencePOJO.ifPresent(this::setDao);
        return searchablePreferencePOJO;
    }

    public List<PreferenceAndPredecessor> _setDao(final List<PreferenceAndPredecessor> preferenceAndPredecessors) {
        preferenceAndPredecessors.forEach(this::setDao);
        return preferenceAndPredecessors;
    }

    public List<PreferenceAndChildren> __setDao(final List<PreferenceAndChildren> preferenceAndChildren) {
        preferenceAndChildren.forEach(this::setDao);
        return preferenceAndChildren;
    }

    public List<SearchablePreferencePOJO> setDao(final List<SearchablePreferencePOJO> searchablePreferencePOJOs) {
        searchablePreferencePOJOs.forEach(this::setDao);
        return searchablePreferencePOJOs;
    }

    public SearchablePreferencePOJO[] setDao(final SearchablePreferencePOJO[] searchablePreferencePOJOs) {
        setDao(Arrays.asList(searchablePreferencePOJOs));
        return searchablePreferencePOJOs;
    }

    public Collection<SearchablePreferencePOJO> setDao(final Collection<SearchablePreferencePOJO> searchablePreferencePOJOs) {
        searchablePreferencePOJOs.forEach(this::setDao);
        return searchablePreferencePOJOs;
    }

    private SearchablePreferencePOJO setDao(final SearchablePreferencePOJO searchablePreferencePOJO) {
        searchablePreferencePOJO.setDao(Optional.of(dao));
        return searchablePreferencePOJO;
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
