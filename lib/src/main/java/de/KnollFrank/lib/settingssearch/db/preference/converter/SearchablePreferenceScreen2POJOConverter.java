package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.BiMap;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.common.Strings;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.HostWithArguments;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

class SearchablePreferenceScreen2POJOConverter {

    public static SearchablePreferenceScreenWithMap convert2POJO(final int id,
                                                                 final Optional<Integer> parentId,
                                                                 final PreferenceScreen preferenceScreen,
                                                                 final PreferenceFragmentCompat hostOfPreferenceScreen,
                                                                 final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter,
                                                                 final Optional<SearchablePreference> predecessorOfPreferenceScreen) {
        final BiMap<SearchablePreference, Preference> searchablePreferences =
                preference2SearchablePreferenceConverter.convert2POJOs(
                        Preferences.getImmediateChildren(preferenceScreen),
                        id,
                        hostOfPreferenceScreen,
                        Optional.empty(),
                        predecessorOfPreferenceScreen);
        return new SearchablePreferenceScreenWithMap(
                new SearchablePreferenceScreen(
                        id,
                        HostWithArguments.of(hostOfPreferenceScreen),
                        Strings.toString(Optional.ofNullable(preferenceScreen.getTitle())),
                        Strings.toString(Optional.ofNullable(preferenceScreen.getSummary())),
                        searchablePreferences.keySet(),
                        parentId),
                searchablePreferences);
    }
}
