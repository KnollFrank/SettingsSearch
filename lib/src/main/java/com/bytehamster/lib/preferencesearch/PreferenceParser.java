package com.bytehamster.lib.preferencesearch;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.XmlRes;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import java.util.List;

public class PreferenceParser {

    private final PreferenceManager preferenceManager;

    @SuppressLint("RestrictedApi")
    public static PreferenceParser fromContext(final Context context) {
        return new PreferenceParser(new PreferenceManager(context));
    }

    public PreferenceParser(final PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    public List<Preference> parsePreferenceScreen(@XmlRes final int preferenceScreen) {
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

    private List<Preference> getPreferences(@XmlRes final int preferenceScreen) {
        return getPreferences(getPreferenceScreen(preferenceScreen));
    }

    @SuppressLint("RestrictedApi")
    private PreferenceScreen getPreferenceScreen(@XmlRes final int resId) {
        return preferenceManager.inflateFromResource(
                preferenceManager.getContext(),
                resId,
                null);
    }
}
