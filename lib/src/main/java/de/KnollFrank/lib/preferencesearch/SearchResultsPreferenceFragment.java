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
        PreferencesRemover.removePreferencesFromTheirParents(preferences);
        ClickListenerSetter.setClickListener(
                preference -> System.out.println("clicking on preference " + preference),
                preferences);
        setPreferencesOnOptionalPreferenceScreen(preferences);
        this.preferences = preferences;
    }

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        final PreferenceScreen preferenceScreen = createPreferenceScreen();
        PreferencesSetter.addPreferences2PreferenceScreen(this.preferences, preferenceScreen);
        setPreferenceScreen(preferenceScreen);
    }

    private static class PreferencesRemover {

        public static void removePreferencesFromTheirParents(final Collection<Preference> preferences) {
            preferences.forEach(PreferencesRemover::removePreferenceFromItsParent);
        }

        private static void removePreferenceFromItsParent(final Preference preference) {
            final PreferenceGroup parent = preference.getParent();
            if (parent != null) {
                parent.removePreference(preference);
            }
        }
    }

    private static class ClickListenerSetter {

        public static void setClickListener(final Consumer<Preference> clickListener,
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
    }

    private PreferenceScreen createPreferenceScreen() {
        return getPreferenceManager().createPreferenceScreen(getPreferenceManager().getContext());
    }

    private void setPreferencesOnOptionalPreferenceScreen(final List<Preference> preferences) {
        this
                .getOptionalPreferenceScreen()
                .ifPresent(
                        preferenceScreen ->
                                PreferencesSetter.setPreferencesOnPreferenceScreen(
                                        preferences,
                                        preferenceScreen));
    }

    private Optional<PreferenceScreen> getOptionalPreferenceScreen() {
        return Optional
                .ofNullable(getPreferenceManager())
                .map(PreferenceManager::getPreferenceScreen);
    }

    private static class PreferencesSetter {

        public static void setPreferencesOnPreferenceScreen(final List<Preference> preferences,
                                                            final PreferenceScreen preferenceScreen) {
            preferenceScreen.removeAll();
            addPreferences2PreferenceScreen(preferences, preferenceScreen);
        }

        public static void addPreferences2PreferenceScreen(final List<Preference> preferences,
                                                           final PreferenceScreen preferenceScreen) {
            preferences.forEach(preferenceScreen::addPreference);
        }
    }
}
