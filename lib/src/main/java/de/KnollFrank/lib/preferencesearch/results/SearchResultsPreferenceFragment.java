package de.KnollFrank.lib.preferencesearch.results;

import static de.KnollFrank.lib.preferencesearch.results.PreferenceScreenForSearchPreparer.preparePreferenceScreenForSearch;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.Navigation;
import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHosts;

// FK-TODO: die PreferenceCategory im Suchergebnis, die den Namen eines PreferenceScreens anzeigt, soll nicht anklickbar sein.
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
        preparePreferenceScreenForSearch(preferenceScreenWithHosts.preferenceScreen);
        this.preferenceScreenWithHosts = preferenceScreenWithHosts;
    }

    private void showPreferenceScreenAndHighlightPreference(final Preference preference) {
        if (preference instanceof PreferenceGroup) {
            return;
        }
        this
                .getHost(preference)
                .ifPresent(
                        host ->
                                Navigation.showPreferenceScreenAndHighlightPreference(
                                        host,
                                        preference.getKey(),
                                        getActivity(),
                                        this.fragmentContainerViewId));
    }

    private Optional<? extends Class<? extends PreferenceFragmentCompat>> getHost(final Preference preference) {
        return preferenceScreenWithHosts
                .preferenceWithHostList
                .stream()
                .filter(preferenceWithHost -> preferenceWithHost.preference.equals(preference))
                .map(preferenceWithHost -> preferenceWithHost.host)
                .findFirst();
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
