package de.KnollFrank.lib.settingssearch.common;

import android.annotation.SuppressLint;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;

import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Preferences {

    private Preferences() {
    }

    public static Preference findPreferenceByKeyOrElseThrow(final List<Preference> preferences,
                                                            final String keyOfPreference) {
        return Preferences
                .findPreferenceByKey(preferences, keyOfPreference)
                .orElseThrow(() -> new NoSuchElementException("can not find preference with key " + keyOfPreference + " within provided preferences."));
    }

    public static Optional<Preference> findPreferenceByKey(final List<Preference> preferences,
                                                           final String keyOfPreference) {
        return preferences
                .stream()
                .filter(preference -> keyOfPreference.equals(preference.getKey()))
                .findFirst();
    }

    public static Preference findPreferenceByKeyOrElseThrow(final PreferenceFragmentCompat hostOfPreference,
                                                            final String keyOfPreference) {
        return Optional
                .<Preference>ofNullable(hostOfPreference.findPreference(keyOfPreference))
                .orElseThrow(() -> new NoSuchElementException("can not find preference with key " + keyOfPreference + " within preferenceFragment " + hostOfPreference));
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

    @SuppressLint("RestrictedApi")
    public static void performClick(final Preference preference) {
        preference.performClick();
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
