package de.KnollFrank.lib.preferencesearch.client;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import de.KnollFrank.lib.preferencesearch.fragment.IFragmentFactory;
import de.KnollFrank.lib.preferencesearch.search.SearchPreferenceFragment;

public class SearchPreferenceFragments {

    private final SearchConfiguration searchConfiguration;
    private final FragmentManager fragmentManager;
    private final IFragmentFactory fragmentFactory;

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                     final FragmentManager fragmentManager,
                                     final IFragmentFactory fragmentFactory) {
        this.searchConfiguration = searchConfiguration;
        this.fragmentManager = fragmentManager;
        this.fragmentFactory = fragmentFactory;
    }

    public void showSearchPreferenceFragment() {
        show(SearchPreferenceFragment.newInstance(this.searchConfiguration, fragmentFactory));
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
