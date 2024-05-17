package com.bytehamster.lib.preferencesearch;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.XmlRes;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.bytehamster.lib.preferencesearch.common.Utils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import java.util.List;
import java.util.stream.Collectors;

class PreferenceParser {

    private final Context context;

    public PreferenceParser(final Context context) {
        this.context = context;
    }

    public List<PreferenceItem> parsePreferences(final List<Integer> preferenceScreens) {
        return getPreferenceItems(preferenceScreens);
    }

    private List<PreferenceItem> getPreferenceItems(final List<Integer> preferenceScreens) {
        final List<List<PreferenceItem>> preferenceItems =
                preferenceScreens
                        .stream()
                        .map(this::getPreferenceItems)
                        .collect(Collectors.toList());
        return Utils.concat(preferenceItems);
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
