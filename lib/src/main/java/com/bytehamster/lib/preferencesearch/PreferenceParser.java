package com.bytehamster.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import java.util.List;

class PreferenceParser {

    private final PreferenceFragments preferenceFragments;

    public PreferenceParser(final PreferenceFragments preferenceFragments) {
        this.preferenceFragments = preferenceFragments;
    }

    // FK-TODO: rename to getPreferences(), also in test methods
    public List<Preference> parsePreferenceScreen(final Class<? extends PreferenceFragmentCompat> preferenceScreen) {
        return getPreferences(preferenceScreen);
    }

    public static List<Preference> getPreferences(final PreferenceGroup preferenceGroup) {
        final Builder<Preference> preferencesBuilder = ImmutableList.builder();
        for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
            final Preference preference = preferenceGroup.getPreference(i);
            preferencesBuilder.add(preference);
            if (preference instanceof PreferenceGroup) {
                preferencesBuilder.addAll(getPreferences((PreferenceGroup) preference));
            }
        }
        return preferencesBuilder.build();
    }

    private List<Preference> getPreferences(final Class<? extends PreferenceFragmentCompat> preferenceScreen) {
        return getPreferences(getPreferenceScreen(preferenceScreen));
    }

    private PreferenceScreen getPreferenceScreen(final Class<? extends PreferenceFragmentCompat> resId) {
        return this
                .preferenceFragments
                .getPreferenceScreenOfFragment(resId.getName())
                .get()
                .preferenceScreen;
    }
}
