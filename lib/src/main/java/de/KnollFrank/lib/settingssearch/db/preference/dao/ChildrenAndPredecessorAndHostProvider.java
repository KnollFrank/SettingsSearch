package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public interface ChildrenAndPredecessorAndHostProvider {

    Set<SearchablePreference> getChildren(SearchablePreference preference);

    // FK-TODO: change to Optional<SearchablePreference> getPredecessor(SearchablePreference preference);
    Map<SearchablePreference, SearchablePreference> getPredecessorByPreference();

    SearchablePreferenceScreen getHost(SearchablePreference preference);
}
