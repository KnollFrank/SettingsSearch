package de.KnollFrank.lib.settingssearch.fragment.navigation;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class PreferencePathPointerFactory {

    public static PreferencePathPointer createPreferencePathPointer(final PreferencePathNavigatorData preferencePathNavigatorData,
                                                                    final Set<SearchablePreference> preferences) {
        return PreferencePathPointer.of(
                SearchablePreferences
                        .getPreferenceFromId(
                                preferencePathNavigatorData.idOfSearchablePreference(),
                                preferences)
                        .getPreferencePath(),
                preferencePathNavigatorData.indexWithinPreferencePath());
    }
}
