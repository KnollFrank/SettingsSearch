package de.KnollFrank.lib.preferencesearch.common;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.List;

public class PreferenceGroups {

    public static List<Preference> getAllPreferences(final PreferenceScreen preferenceScreen) {
        return ImmutableList
                .<Preference>builder()
                .add(preferenceScreen)
                .addAll(getAllChildren(preferenceScreen))
                .build();
    }

    public static List<Preference> getAllChildren(final PreferenceGroup preferenceGroup) {
        final ImmutableList.Builder<Preference> preferencesBuilder = ImmutableList.builder();
        for (final Preference preference : getDirectChildren(preferenceGroup)) {
            preferencesBuilder.add(preference);
            if (preference instanceof PreferenceGroup) {
                preferencesBuilder.addAll(getAllChildren((PreferenceGroup) preference));
            }
        }
        return preferencesBuilder.build();
    }

    public static List<Preference> getDirectChildren(final PreferenceGroup preferenceGroup) {
        return ImmutableList.copyOf(getDirectChildrenIterator(preferenceGroup));
    }

    private static Iterator<Preference> getDirectChildrenIterator(final PreferenceGroup preferenceGroup) {
        return new Iterator<>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < preferenceGroup.getPreferenceCount();
            }

            @Override
            public Preference next() {
                return preferenceGroup.getPreference(i++);
            }
        };
    }
}
