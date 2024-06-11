package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.ImmutableList;

import java.util.List;

class PreferenceProvider {

    private final PreferenceFragments preferenceFragments;

    public PreferenceProvider(final PreferenceFragments preferenceFragments) {
        this.preferenceFragments = preferenceFragments;
    }

    public static List<Preference> getPreferences(final PreferenceGroup preferenceGroup) {
        final ImmutableList.Builder<Preference> preferencesBuilder = ImmutableList.builder();
        for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
            final Preference preference = preferenceGroup.getPreference(i);
            preferencesBuilder.add(preference);
            if (preference instanceof PreferenceGroup) {
                preferencesBuilder.addAll(getPreferences((PreferenceGroup) preference));
            }
        }
        return preferencesBuilder.build();
    }

    public List<Preference> getPreferences(final Class<? extends PreferenceFragmentCompat> preferenceScreen) {
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
