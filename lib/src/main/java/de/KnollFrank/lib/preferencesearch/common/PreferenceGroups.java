package de.KnollFrank.lib.preferencesearch.common;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;

import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.List;

public class PreferenceGroups {

    public static List<Preference> getImmediateChildren(final PreferenceGroup preferenceGroup) {
        return ImmutableList.copyOf(getImmediateChildrenIterator(preferenceGroup));
    }

    private static Iterator<Preference> getImmediateChildrenIterator(final PreferenceGroup preferenceGroup) {
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
