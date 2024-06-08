package de.KnollFrank.lib.preferencesearch;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import de.KnollFrank.lib.preferencesearch.preference.IClickablePreference;

public class SearchResultsPreferenceFragment extends BaseSearchPreferenceFragment {

    private List<Preference> preferences = Collections.emptyList();

    public void setPreferences(final List<Preference> preferences) {
        removePreferencesFromTheirParents(preferences);
        setClickListener(
                preference -> System.out.println("clicking on preference " + preference),
                preferences);
        this
                .getOptionalPreferenceScreen()
                .ifPresent(preferenceScreen -> setPreferences(preferences, preferenceScreen));
        this.preferences = preferences;
    }

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        final PreferenceScreen preferenceScreen = createPreferenceScreen();
        addPreferences(preferences, preferenceScreen);
        setPreferenceScreen(preferenceScreen);
    }

    private static void removePreferencesFromTheirParents(final Collection<Preference> preferences) {
        preferences.forEach(SearchResultsPreferenceFragment::removePreferenceFromItsParent);
    }

    private static void removePreferenceFromItsParent(final Preference preference) {
        final PreferenceGroup parent = preference.getParent();
        if (parent != null) {
            parent.removePreference(preference);
        }
    }

    private static void setClickListener(final Consumer<Preference> clickListener,
                                         final List<Preference> preferences) {
        for (final Preference preference : preferences) {
            setClickListener(clickListener, preference);
        }
    }

    private static void setClickListener(final Consumer<Preference> clickListener,
                                         final Preference preference) {
        preference.setEnabled(false);
        preference.setShouldDisableView(false);
        if (preference instanceof IClickablePreference) {
            ((IClickablePreference) preference).setClickListener(clickListener);
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
                                       final PreferenceScreen preferenceScreen) {
        preferences.forEach(preferenceScreen::addPreference);
    }
}
