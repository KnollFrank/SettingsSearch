package de.KnollFrank.lib.settingssearch.fragment.navigation;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceDAO;

class PreferencePathPointerFactory {

    public static PreferencePathPointer createPreferencePathPointer(final PreferencePathNavigatorData preferencePathNavigatorData,
                                                                    final SearchablePreferenceDAO searchablePreferenceDAO) {
        return PreferencePathPointer.of(
                searchablePreferenceDAO
                        .findPreferenceById(preferencePathNavigatorData.idOfSearchablePreference())
                        .orElseThrow()
                        .getPreferencePath(searchablePreferenceDAO),
                preferencePathNavigatorData.indexWithinPreferencePath());
    }
}
