package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public interface ChildrenAndPredecessorAndHostProvider {

    Set<SearchablePreference> getChildren(SearchablePreference preference);

    Optional<SearchablePreference> getPredecessor(SearchablePreference preference);

    SearchablePreferenceScreen getHost(SearchablePreference preference);
}
