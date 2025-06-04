package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.BiMap;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.common.Strings;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class PreferenceScreen2SearchablePreferenceScreenConverter {

    private final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter;

    public PreferenceScreen2SearchablePreferenceScreenConverter(final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter) {
        this.preference2SearchablePreferenceConverter = preference2SearchablePreferenceConverter;
    }

    public SearchablePreferenceScreenWithMap convertPreferenceScreen(
            final PreferenceScreen preferenceScreen,
            final PreferenceFragmentCompat hostOfPreferenceScreen,
            final String id,
            final Optional<String> parentId,
            final Optional<SearchablePreferenceEntity> predecessorOfPreferenceScreen) {
        final BiMap<SearchablePreferenceEntity, Preference> searchablePreferences =
                preference2SearchablePreferenceConverter.convertPreferences(
                        Preferences.getImmediateChildren(preferenceScreen),
                        id,
                        hostOfPreferenceScreen,
                        Optional.empty(),
                        predecessorOfPreferenceScreen);
        return new SearchablePreferenceScreenWithMap(
                new SearchablePreferenceScreenEntity(
                        id,
                        hostOfPreferenceScreen.getClass(),
                        Strings.toString(Optional.ofNullable(preferenceScreen.getTitle())),
                        Strings.toString(Optional.ofNullable(preferenceScreen.getSummary())),
                        searchablePreferences.keySet(),
                        parentId),
                searchablePreferences);
    }
}
