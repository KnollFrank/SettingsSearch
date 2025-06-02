package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndChildren;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndPredecessor;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

class SearchablePreferenceDAOSetter {

    private final SearchablePreferenceEntity.DbDataProvider dao;

    public SearchablePreferenceDAOSetter(final SearchablePreferenceEntity.DbDataProvider dao) {
        this.dao = dao;
    }

    public Optional<SearchablePreferenceEntity> setDao(final Optional<SearchablePreferenceEntity> searchablePreference) {
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

    public List<SearchablePreferenceEntity> setDao(final List<SearchablePreferenceEntity> searchablePreferenceEntities) {
        searchablePreferenceEntities.forEach(this::setDao);
        return searchablePreferenceEntities;
    }

    public SearchablePreferenceEntity[] setDao(final SearchablePreferenceEntity[] searchablePreferenceEntities) {
        setDao(Arrays.asList(searchablePreferenceEntities));
        return searchablePreferenceEntities;
    }

    public Collection<SearchablePreferenceEntity> setDao(final Collection<SearchablePreferenceEntity> searchablePreferenceEntities) {
        searchablePreferenceEntities.forEach(this::setDao);
        return searchablePreferenceEntities;
    }

    public Set<SearchablePreferenceEntity> setDao(final Set<SearchablePreferenceEntity> searchablePreferenceEntities) {
        searchablePreferenceEntities.forEach(this::setDao);
        return searchablePreferenceEntities;
    }

    public SearchablePreferenceEntity setDao(final SearchablePreferenceEntity searchablePreferenceEntity) {
        searchablePreferenceEntity.setDao(dao);
        return searchablePreferenceEntity;
    }

    public void setDao(final Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> childrenByPreference) {
        childrenByPreference.forEach(
                (preference, children) -> {
                    setDao(preference);
                    setDao(children);
                });
    }

    public void _setDao(final Map<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>> predecessorByPreference) {
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
