package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.BiMap;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
            final PreferenceScreen preferenceScreen,
            final PreferenceFragmentCompat hostOfPreferenceScreen,
            final String id,
            final Optional<SearchablePreference> predecessorOfPreferenceScreen,
            final Locale locale) {
        final BiMap<SearchablePreference, Preference> searchablePreferences =
                preferenceToSearchablePreferenceConverter.convertPreferences(
                        Preferences.getImmediateChildren(preferenceScreen),
                        List.of(),
                        id,
                        hostOfPreferenceScreen,
                        predecessorOfPreferenceScreen,
                        locale);
        return new SearchablePreferenceScreenWithMap(
                new SearchablePreferenceScreen(
                        id,
                        hostOfPreferenceScreen.getClass(),
                        Strings.toString(Optional.ofNullable(preferenceScreen.getTitle())),
                        Strings.toString(Optional.ofNullable(preferenceScreen.getSummary())),
                        searchablePreferences.keySet()),
                searchablePreferences);
    }
}
