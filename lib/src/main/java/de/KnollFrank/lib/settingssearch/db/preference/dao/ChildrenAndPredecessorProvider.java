package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.List;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public interface ChildrenAndPredecessorProvider {

    Map<SearchablePreference, List<SearchablePreference>> getChildrenByPreference();

    Map<SearchablePreference, SearchablePreference> getPredecessorByPreference();
}
