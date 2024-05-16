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

import java.util.ArrayList;
import java.util.List;

class PreferenceParser {

    private final Context context;
    private final List<PreferenceItem> preferenceItems = new ArrayList<>();

    public PreferenceParser(final Context context) {
        this.context = context;
    }

    public void addResourceFile(@XmlRes final int preferenceScreen) {
        preferenceItems.addAll(getPreferenceItems(preferenceScreen));
    }

    public void addPreferenceItems(final List<PreferenceItem> preferenceItems) {
        this.preferenceItems.addAll(preferenceItems);
    }

    public List<PreferenceItem> getPreferenceItems() {
        return preferenceItems;
    }

    private List<PreferenceItem> getPreferenceItems(@XmlRes final int preferenceScreen) {
        @SuppressLint("RestrictedApi") final PreferenceManager preferenceManager = new PreferenceManager(context);
        @SuppressLint("RestrictedApi") final PreferenceScreen _preferenceScreen = preferenceManager.inflateFromResource(preferenceManager.getContext(), preferenceScreen, null);
        return new SearchConfiguration().indexItems(
                getPreferences(_preferenceScreen),
                preferenceScreen);
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
