package de.KnollFrank.lib.preferencesearch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SearchPreferenceFragments {

    private final SearchConfiguration searchConfiguration;

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration) {
        this.searchConfiguration = searchConfiguration;
    }

    public void showSearchPreferenceFragment() {
        show(createSearchPreferenceFragment());
    }

    private SearchPreferenceFragment createSearchPreferenceFragment() {
        final SearchPreferenceFragment searchPreferenceFragment = new SearchPreferenceFragment();
        searchPreferenceFragment.setArguments(SearchConfigurations.toBundle(this.searchConfiguration));
        return searchPreferenceFragment;
    }

    private void show(final Fragment fragment) {
        this
                .getSupportFragmentManager()
                .beginTransaction()
                .add(
                        this.searchConfiguration.fragmentContainerViewId,
                        fragment,
                        fragment.getClass().getName())
                .commit();
    }

    private FragmentManager getSupportFragmentManager() {
        return this.searchConfiguration.activity.get().getSupportFragmentManager();
    }
}
