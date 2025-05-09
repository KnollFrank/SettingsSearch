package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public interface ChildrenAndPredecessorProvider {

    Map<SearchablePreference, Set<SearchablePreference>> getChildrenByPreference();

    Map<SearchablePreference, SearchablePreference> getPredecessorByPreference();
}
