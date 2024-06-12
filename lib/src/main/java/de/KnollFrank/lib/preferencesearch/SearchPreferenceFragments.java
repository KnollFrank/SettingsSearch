package de.KnollFrank.lib.preferencesearch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import de.KnollFrank.lib.preferencesearch.search.SearchPreferenceFragment;

public class SearchPreferenceFragments {

    private final SearchConfiguration searchConfiguration;
    private final FragmentManager fragmentManager;

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                     final FragmentManager fragmentManager) {
        this.searchConfiguration = searchConfiguration;
        this.fragmentManager = fragmentManager;
    }

    public void showSearchPreferenceFragment() {
        show(SearchPreferenceFragment.newInstance(this.searchConfiguration));
    }

    private void show(final Fragment fragment) {
        this
                .fragmentManager
                .beginTransaction()
                .replace(
                        this.searchConfiguration.fragmentContainerViewId,
                        fragment,
                        fragment.getClass().getName())
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }
}
