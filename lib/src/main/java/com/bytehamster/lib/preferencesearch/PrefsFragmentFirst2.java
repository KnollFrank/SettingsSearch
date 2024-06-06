package com.bytehamster.lib.preferencesearch;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// FK-TODO: rename to SearchResultsPreferenceFragment
public class PrefsFragmentFirst2 extends BaseSearchPreferenceFragment {

    private List<Preference> preferences = Collections.emptyList();

    // so kann man den in einem PreferenceFragmentCompat angezeigten PreferenceScreen mit Preferences aktualisieren.
    public void setPreferences(final List<Preference> preferences) {
        removePreferencesFromTheirParents(preferences);
        this
                .getOptionalPreferenceScreen()
                .ifPresent(preferenceScreen -> setPreferences(preferences, preferenceScreen));
        this.preferences = preferences;
    }

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        final PreferenceScreen screen = createPreferenceScreen();
        addPreferences(preferences, screen);
        setPreferenceScreen(screen);
    }

    private static void removePreferencesFromTheirParents(final Collection<Preference> preferences) {
        preferences.forEach(PrefsFragmentFirst2::removePreferenceFromItsParent);
    }

    private static void removePreferenceFromItsParent(final Preference preference) {
        final PreferenceGroup parent = preference.getParent();
        if (parent != null) {
            parent.removePreference(preference);
        }
    }

    private PreferenceScreen createPreferenceScreen() {
        return getPreferenceManager().createPreferenceScreen(getPreferenceManager().getContext());
    }

    private Optional<PreferenceScreen> getOptionalPreferenceScreen() {
        return Optional
                .ofNullable(getPreferenceManager())
                .map(PreferenceManager::getPreferenceScreen);
    }

    private static void setPreferences(final List<Preference> preferences,
                                       final PreferenceScreen preferenceScreen) {
        preferenceScreen.removeAll();
        addPreferences(preferences, preferenceScreen);
    }

    private static void addPreferences(final List<Preference> preferences,
                                       final PreferenceScreen screen) {
        preferences.forEach(screen::addPreference);
    }
}
