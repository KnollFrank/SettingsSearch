package com.bytehamster.lib.preferencesearch;

import android.annotation.SuppressLint;

import androidx.annotation.XmlRes;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import java.util.List;

class PreferenceParser {

    private final PreferenceManager preferenceManager;

    public PreferenceParser(final PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    public List<Preference> parsePreferenceScreen(@XmlRes final int preferenceScreen) {
        return getPreferences(preferenceScreen);
    }

    private List<Preference> getPreferences(@XmlRes final int preferenceScreen) {
        @SuppressLint("RestrictedApi") final PreferenceScreen _preferenceScreen =
                preferenceManager.inflateFromResource(
                        preferenceManager.getContext(),
                        preferenceScreen,
                        null);
        return getPreferences(_preferenceScreen);
    }

    private static List<Preference> getPreferences(final PreferenceGroup preferenceGroup) {
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
}
