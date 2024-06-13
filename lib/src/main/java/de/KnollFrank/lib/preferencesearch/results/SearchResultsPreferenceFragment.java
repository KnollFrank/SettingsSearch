package de.KnollFrank.lib.preferencesearch.results;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import de.KnollFrank.lib.preferencesearch.Navigation;
import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHosts;

public class SearchResultsPreferenceFragment extends PreferenceFragmentCompat {

    private PreferenceScreenWithHosts preferenceScreenWithHosts;
    private @IdRes int fragmentContainerViewId;

    public static SearchResultsPreferenceFragment newInstance(final @IdRes int fragmentContainerViewId,
                                                              final PreferenceScreenWithHosts preferenceScreenWithHosts) {
        final SearchResultsPreferenceFragment searchResultsPreferenceFragment = Factory.newInstance(fragmentContainerViewId);
        searchResultsPreferenceFragment.setPreferenceScreenWithHosts(preferenceScreenWithHosts);
        return searchResultsPreferenceFragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        new Factory().setInstanceVariables();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(@Nullable final Bundle savedInstanceState, @Nullable final String rootKey) {
        setPreferenceScreen(this.preferenceScreenWithHosts.preferenceScreen);
    }

    @NonNull
    @Override
    protected Adapter onCreateAdapter(@NonNull final PreferenceScreen preferenceScreen) {
        return new ClickablePreferenceGroupAdapter(
                preferenceScreen,
                this::showPreferenceScreenAndHighlightPreference);
    }

    private void setPreferenceScreenWithHosts(final PreferenceScreenWithHosts preferenceScreenWithHosts) {
        this.preferenceScreenWithHosts = preferenceScreenWithHosts;
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
        return preferenceScreenWithHosts
                .preferenceWithHostList
                .stream()
                .filter(preferenceWithHost -> preferenceWithHost.preference.equals(preference))
                .findFirst()
                .get()
                .host;
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
