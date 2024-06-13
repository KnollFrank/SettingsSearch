package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.List;

import de.KnollFrank.lib.preferencesearch.common.PreferenceGroups;

public class PreferenceProvider {

    private final PreferenceFragments preferenceFragments;

    public PreferenceProvider(final PreferenceFragments preferenceFragments) {
        this.preferenceFragments = preferenceFragments;
    }

    public List<Preference> getPreferences(final Class<? extends PreferenceFragmentCompat> preferenceScreen) {
        return PreferenceGroups.getAllChildren(getPreferenceScreen(preferenceScreen));
    }

    private PreferenceScreen getPreferenceScreen(final Class<? extends PreferenceFragmentCompat> resId) {
        return this
                .preferenceFragments
                .getPreferenceScreenOfFragment(resId.getName())
                .get()
                .preferenceScreen;
    }
}
