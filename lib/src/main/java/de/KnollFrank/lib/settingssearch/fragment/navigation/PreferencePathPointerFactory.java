package de.KnollFrank.lib.settingssearch.fragment.navigation;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceEntityDAO;

class PreferencePathPointerFactory {

    public static PreferencePathPointer createPreferencePathPointer(final PreferencePathNavigatorData preferencePathNavigatorData,
                                                                    final SearchablePreferenceEntityDAO searchablePreferenceDAO) {
        // FK-FIXME:
        return null;
//        return PreferencePathPointer.of(
//                searchablePreferenceDAO
//                        .findPreferenceById(preferencePathNavigatorData.idOfSearchablePreference())
//                        .orElseThrow()
//                        .getPreferencePath(searchablePreferenceDAO),
//                preferencePathNavigatorData.indexWithinPreferencePath());
    }
}
