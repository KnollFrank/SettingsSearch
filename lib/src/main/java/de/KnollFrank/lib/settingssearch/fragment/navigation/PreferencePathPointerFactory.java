package de.KnollFrank.lib.settingssearch.fragment.navigation;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.graph.PojoGraphs;

class PreferencePathPointerFactory {

    public static PreferencePathPointer createPreferencePathPointer(final PreferencePathNavigatorData preferencePathNavigatorData,
                                                                    final SearchablePreferenceScreenGraphDAO graphDAO,
                                                                    final Locale locale) {
        return PreferencePathPointer.of(
                getPreferencePath(preferencePathNavigatorData, graphDAO, locale),
                preferencePathNavigatorData.indexWithinPreferencePath());
    }

    private static PreferencePath getPreferencePath(final PreferencePathNavigatorData preferencePathNavigatorData,
                                                    final SearchablePreferenceScreenGraphDAO graphDAO,
                                                    final Locale locale) {
        return SearchablePreferences
                .findPreferenceById(
                        PojoGraphs.getPreferences(graphDAO.findGraphById(locale).orElseThrow().graph()),
                        preferencePathNavigatorData.idOfSearchablePreference())
                .orElseThrow()
                .getPreferencePath();
    }
}
