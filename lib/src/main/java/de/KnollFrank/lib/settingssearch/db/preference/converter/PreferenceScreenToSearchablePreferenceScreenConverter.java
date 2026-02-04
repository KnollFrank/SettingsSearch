package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.Preference;

import com.google.common.collect.BiMap;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.common.Strings;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class PreferenceScreenToSearchablePreferenceScreenConverter {

    private final PreferenceToSearchablePreferenceConverter preferenceToSearchablePreferenceConverter;

    public PreferenceScreenToSearchablePreferenceScreenConverter(final PreferenceToSearchablePreferenceConverter preferenceToSearchablePreferenceConverter) {
        this.preferenceToSearchablePreferenceConverter = preferenceToSearchablePreferenceConverter;
    }

    public SearchablePreferenceScreenWithMap convertPreferenceScreen(
            final PreferenceScreenOfHostOfActivity preferenceScreen,
            final String id) {
        final BiMap<SearchablePreference, Preference> searchablePreferences =
                preferenceToSearchablePreferenceConverter.convertPreferences(
                        Preferences.getImmediateChildren(preferenceScreen.preferenceScreen()),
                        List.of(),
                        id,
                        preferenceScreen.hostOfPreferenceScreen());
        return new SearchablePreferenceScreenWithMap(
                new SearchablePreferenceScreen(
                        id,
                        preferenceScreen
                                .asPreferenceFragmentOfActivity()
                                .asPreferenceFragmentClassOfActivity(),
                        Strings.toString(Optional.ofNullable(preferenceScreen.preferenceScreen().getTitle())),
                        Strings.toString(Optional.ofNullable(preferenceScreen.preferenceScreen().getSummary())),
                        searchablePreferences.keySet()),
                searchablePreferences);
    }
}
