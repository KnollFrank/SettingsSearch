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
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.preference.IClickablePreference;

public class SearchResultsPreferenceFragment extends BaseSearchPreferenceFragment {

    private List<PreferenceWithHost> preferenceWithHostList = Collections.emptyList();

    public void setPreferenceWithHostList(final List<PreferenceWithHost> preferenceWithHostList) {
        final List<Preference> preferences = getPreferences(preferenceWithHostList);
        PreferencesRemover.removePreferencesFromTheirParents(preferences);
        ClickListenerSetter.setPreferenceClickListener(
                preferenceWithHost ->
                        ((SearchPreferenceResultListener) getActivity())
                                .onSearchResultClicked(getSearchPreferenceResult(preferenceWithHost)),
                preferenceWithHostList);
        setPreferencesOnOptionalPreferenceScreen(preferences);
        this.preferenceWithHostList = preferenceWithHostList;
    }

    private List<Preference> getPreferences(final List<PreferenceWithHost> preferenceWithHostList) {
        return preferenceWithHostList
                .stream()
                .map(preferenceWithHost -> preferenceWithHost.preference)
                .collect(Collectors.toList());
    }

    private static SearchPreferenceResult getSearchPreferenceResult(final PreferenceWithHost preferenceWithHost) {
        return new SearchPreferenceResult(
                preferenceWithHost.preference.getKey(),
                preferenceWithHost.host
        );
    }

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        final PreferenceScreen preferenceScreen = createPreferenceScreen();
        PreferencesSetter.addPreferences2PreferenceScreen(
                getPreferences(this.preferenceWithHostList),
                preferenceScreen);
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

        public static void setPreferenceClickListener(final Consumer<PreferenceWithHost> preferenceClickListener,
                                                      final List<PreferenceWithHost> preferenceWithHostList) {
            for (final PreferenceWithHost preferenceWithHost : preferenceWithHostList) {
                setPreferenceClickListener(preferenceClickListener, preferenceWithHost);
            }
        }

        private static void setPreferenceClickListener(final Consumer<PreferenceWithHost> preferenceClickListener,
                                                       final PreferenceWithHost preferenceWithHost) {
            preparePreferenceClickListener(preferenceWithHost.preference);
            if (preferenceWithHost.preference instanceof final IClickablePreference clickablePreference) {
                clickablePreference.setPreferenceClickListenerAndHost(preferenceClickListener, preferenceWithHost.host);
            }
        }

        private static void preparePreferenceClickListener(final Preference preference) {
            preference.setEnabled(false);
            preference.setShouldDisableView(false);
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
