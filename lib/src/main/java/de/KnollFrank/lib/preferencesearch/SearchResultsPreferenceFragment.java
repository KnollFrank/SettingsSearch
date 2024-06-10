package de.KnollFrank.lib.preferencesearch;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SearchResultsPreferenceFragment extends PreferenceFragmentCompat {

    private List<PreferenceWithHost> preferenceWithHostList = Collections.emptyList();
    private @IdRes int fragmentContainerViewId;

    public static SearchResultsPreferenceFragment newInstance(final @IdRes int fragmentContainerViewId) {
        return Factory.newInstance(fragmentContainerViewId);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Factory().setInstanceVariables();
    }

    public void setPreferenceWithHostList(final List<PreferenceWithHost> preferenceWithHostList) {
        final List<Preference> preferences = getPreferences(preferenceWithHostList);
        PreferencePreparer.preparePreferences(preferences);
        setPreferencesOnOptionalPreferenceScreen(preferences);
        this.preferenceWithHostList = preferenceWithHostList;
    }

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        final PreferenceScreen preferenceScreen = createPreferenceScreen();
        PreferencesSetter.addPreferences2PreferenceScreen(
                getPreferences(this.preferenceWithHostList),
                preferenceScreen);
        setPreferenceScreen(preferenceScreen);
    }

    @NonNull
    @Override
    protected Adapter onCreateAdapter(@NonNull final PreferenceScreen preferenceScreen) {
        return new SearchPreferenceGroupAdapter(
                preferenceScreen,
                this::showPreferenceScreenAndHighlightPreference,
                this::getPreferenceWithHost);
    }

    private void showPreferenceScreenAndHighlightPreference(final PreferenceWithHost preferenceWithHost) {
        final SearchPreferenceResult searchPreferenceResult = getSearchPreferenceResult(preferenceWithHost);
        Navigation.showPreferenceScreenAndHighlightPreference(
                searchPreferenceResult.getPreferenceFragmentClass().getName(),
                searchPreferenceResult.getKey(),
                getActivity(),
                this.fragmentContainerViewId);
    }

    private PreferenceWithHost getPreferenceWithHost(final Preference preference) {
        return preferenceWithHostList
                .stream()
                .filter(preferenceWithHost -> preferenceWithHost.preference.equals(preference))
                .findFirst()
                .get();
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

    private static class PreferencePreparer {

        public static void preparePreferences(final List<Preference> preferences) {
            preferences.forEach(PreferencePreparer::preparePreference);
        }

        private static void preparePreference(final Preference preference) {
            preference.setEnabled(false);
            preference.setShouldDisableView(false);
            removePreferenceFromItsParent(preference);
        }

        private static void removePreferenceFromItsParent(final Preference preference) {
            final PreferenceGroup parent = preference.getParent();
            if (parent != null) {
                parent.removePreference(preference);
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

    private class Factory {

        private static final String FRAGMENT_CONTAINER_VIEW_ID = "fragmentContainerViewId";

        public static SearchResultsPreferenceFragment newInstance(final @IdRes int fragmentContainerViewId) {
            final SearchResultsPreferenceFragment fragment = new SearchResultsPreferenceFragment();
            fragment.setArguments(createArguments(fragmentContainerViewId));
            return fragment;
        }

        public void setInstanceVariables() {
            SearchResultsPreferenceFragment.this.fragmentContainerViewId =
                    requireArguments().getInt(FRAGMENT_CONTAINER_VIEW_ID);
        }

        private static Bundle createArguments(final @IdRes int fragmentContainerViewId) {
            final Bundle bundle = new Bundle();
            bundle.putInt(FRAGMENT_CONTAINER_VIEW_ID, fragmentContainerViewId);
            return bundle;
        }
    }
}
