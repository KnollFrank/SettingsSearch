package de.KnollFrank.lib.preferencesearch.common;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.List;

public class Preferences {

    public static List<Preference> getAllPreferences(final PreferenceScreen preferenceScreen) {
        return ImmutableList
                .<Preference>builder()
                .add(preferenceScreen)
                .addAll(getAllChildren(preferenceScreen))
                .build();
    }

    public static List<Preference> getAllChildren(final PreferenceGroup preferenceGroup) {
        final ImmutableList.Builder<Preference> childrenBuilder = ImmutableList.builder();
        for (final Preference child : getDirectChildren(preferenceGroup)) {
            childrenBuilder.add(child);
            if (child instanceof PreferenceGroup) {
                childrenBuilder.addAll(getAllChildren((PreferenceGroup) child));
            }
        }
        return childrenBuilder.build();
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
