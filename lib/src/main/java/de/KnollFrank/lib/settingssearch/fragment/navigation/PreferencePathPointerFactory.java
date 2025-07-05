package de.KnollFrank.lib.settingssearch.fragment.navigation;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.graph.PojoGraphs;

class PreferencePathPointerFactory {

    public static PreferencePathPointer createPreferencePathPointer(final PreferencePathNavigatorData preferencePathNavigatorData,
                                                                    final SearchablePreferenceScreenGraphDAO graphDAO) {
        return PreferencePathPointer.of(
                getPreferencePath(preferencePathNavigatorData, graphDAO),
                preferencePathNavigatorData.indexWithinPreferencePath());
    }

    private static PreferencePath getPreferencePath(final PreferencePathNavigatorData preferencePathNavigatorData,
                                                    final SearchablePreferenceScreenGraphDAO graphDAO) {
        return SearchablePreferences
                .findPreferenceById(
                        PojoGraphs.getPreferences(graphDAO.load().orElseThrow()),
                        preferencePathNavigatorData.idOfSearchablePreference())
                .orElseThrow()
                .getPreferencePath();
    }
}
