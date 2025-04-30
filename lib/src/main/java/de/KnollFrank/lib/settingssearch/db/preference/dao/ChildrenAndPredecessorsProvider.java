package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndChildren;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndPredecessor;

public interface ChildrenAndPredecessorsProvider {

    List<PreferenceAndChildren> getPreferencesAndChildren();

    List<PreferenceAndPredecessor> getPreferencesAndPredecessors();
}
