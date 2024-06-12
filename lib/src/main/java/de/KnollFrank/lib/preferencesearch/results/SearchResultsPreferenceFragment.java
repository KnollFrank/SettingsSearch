package de.KnollFrank.lib.preferencesearch.results;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.Navigation;
import de.KnollFrank.lib.preferencesearch.PreferenceWithHost;

public class SearchResultsPreferenceFragment extends PreferenceFragmentCompat {

    // FK-TODO: make preferenceWithHostList of type PreferenceScreen instead of List<PreferenceWithHost>
    private List<PreferenceWithHost> preferenceWithHostList = Collections.emptyList();
    private @IdRes int fragmentContainerViewId;

    public static SearchResultsPreferenceFragment newInstance(final @IdRes int fragmentContainerViewId) {
        return Factory.newInstance(fragmentContainerViewId);
    }

    public void setPreferenceWithHostList(final List<PreferenceWithHost> preferenceWithHostList) {
        final List<Preference> preferences = getPreferences(preferenceWithHostList);
        PreferencePreparer.preparePreferences(preferences);
        setPreferencesOnOptionalPreferenceScreen(preferences);
        this.preferenceWithHostList = preferenceWithHostList;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Factory().setInstanceVariables();
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
        return new ClickablePreferenceGroupAdapter(
                preferenceScreen,
                this::showPreferenceScreenAndHighlightPreference);
    }

    private void showPreferenceScreenAndHighlightPreference(final Preference preference) {
        final Class<? extends PreferenceFragmentCompat> host = getHost(preference);
        Navigation.showPreferenceScreenAndHighlightPreference(
                host.getName(),
                preference.getKey(),
                getActivity(),
                this.fragmentContainerViewId);
    }

    private Class<? extends PreferenceFragmentCompat> getHost(final Preference preference) {
        return preferenceWithHostList
                .stream()
                .filter(preferenceWithHost -> preferenceWithHost.preference.equals(preference))
                .findFirst()
                .get()
                .host;
    }

    private List<Preference> getPreferences(final List<PreferenceWithHost> preferenceWithHostList) {
        return preferenceWithHostList
                .stream()
                .map(preferenceWithHost -> preferenceWithHost.preference)
                .collect(Collectors.toList());
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
