package de.KnollFrank.lib.settingssearch.common;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;

import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Preferences {

    public static Preference findPreferenceOrElseThrow(final PreferenceFragmentCompat hostOfPreference,
                                                       final String keyOfPreference) {
        return Objects.requireNonNull(
                hostOfPreference.findPreference(keyOfPreference),
                () -> "can not find preference with key " + keyOfPreference + " within preferenceFragment " + hostOfPreference);
    }

    public static List<Preference> getChildrenRecursively(final PreferenceGroup preferenceGroup) {
        final ImmutableList.Builder<Preference> childrenBuilder = ImmutableList.builder();
        for (final Preference child : getImmediateChildren(preferenceGroup)) {
            childrenBuilder.add(child);
            if (child instanceof final PreferenceGroup childPreferenceGroup) {
                childrenBuilder.addAll(getChildrenRecursively(childPreferenceGroup));
            }
        }
        return childrenBuilder.build();
    }

    public static List<Preference> getImmediateChildren(final PreferenceGroup preferenceGroup) {
        return ImmutableList.copyOf(getImmediateChildrenIterator(preferenceGroup));
    }

    private static Iterator<Preference> getImmediateChildrenIterator(final PreferenceGroup preferenceGroup) {
        return new Iterator<>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < preferenceGroup.getPreferenceCount();
            }

            @Override
            public Preference next() {
                return preferenceGroup.getPreference(index++);
            }
        };
    }
}
