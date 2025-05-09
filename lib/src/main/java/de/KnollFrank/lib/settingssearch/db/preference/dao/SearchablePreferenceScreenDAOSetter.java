package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenAndAllPreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenAndChildren;

class SearchablePreferenceScreenDAOSetter {

    private final SearchablePreferenceScreenDAO dao;
    private final SearchablePreferenceDAOSetter searchablePreferenceDAOSetter;

    public SearchablePreferenceScreenDAOSetter(final SearchablePreferenceScreenDAO dao,
                                               final SearchablePreferenceDAOSetter searchablePreferenceDAOSetter) {
        this.dao = dao;
        this.searchablePreferenceDAOSetter = searchablePreferenceDAOSetter;
    }

    public List<SearchablePreferenceScreen> setDao(final List<SearchablePreferenceScreen> searchablePreferenceScreens) {
        searchablePreferenceScreens.forEach(this::setDao);
        return searchablePreferenceScreens;
    }

    public List<SearchablePreferenceScreenAndAllPreferences> __setDao(final List<SearchablePreferenceScreenAndAllPreferences> searchablePreferenceScreenAndAllPreferencesList) {
        searchablePreferenceScreenAndAllPreferencesList.forEach(this::setDao);
        return searchablePreferenceScreenAndAllPreferencesList;
    }

    public Optional<SearchablePreferenceScreen> setDao(final Optional<SearchablePreferenceScreen> searchablePreferenceScreen) {
        searchablePreferenceScreen.ifPresent(this::setDao);
        return searchablePreferenceScreen;
    }

    public SearchablePreferenceScreen setDao(final SearchablePreferenceScreen searchablePreferenceScreen) {
        searchablePreferenceScreen.setDao(dao);
        return searchablePreferenceScreen;
    }

    public List<SearchablePreferenceScreenAndChildren> ___setDao(final List<SearchablePreferenceScreenAndChildren> searchablePreferenceScreenAndChildrenList) {
        searchablePreferenceScreenAndChildrenList.forEach(this::setDao);
        return searchablePreferenceScreenAndChildrenList;
    }

    private SearchablePreferenceScreenAndAllPreferences setDao(final SearchablePreferenceScreenAndAllPreferences searchablePreferenceScreenAndAllPreferences) {
        setDao(searchablePreferenceScreenAndAllPreferences.searchablePreferenceScreen());
        searchablePreferenceDAOSetter.setDao(searchablePreferenceScreenAndAllPreferences.allPreferences());
        return searchablePreferenceScreenAndAllPreferences;
    }

    private SearchablePreferenceScreenAndChildren setDao(final SearchablePreferenceScreenAndChildren searchablePreferenceScreenAndChildren) {
        setDao(searchablePreferenceScreenAndChildren.searchablePreferenceScreen());
        setDao(searchablePreferenceScreenAndChildren.children());
        return searchablePreferenceScreenAndChildren;
    }
}
