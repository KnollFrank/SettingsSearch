package de.KnollFrank.lib.preferencesearch.client;

import androidx.fragment.app.FragmentManager;

import de.KnollFrank.lib.preferencesearch.fragment.FragmentFactory;
import de.KnollFrank.lib.preferencesearch.fragment.Navigation;
import de.KnollFrank.lib.preferencesearch.search.SearchPreferenceFragment;

public class SearchPreferenceFragments {

    private final SearchConfiguration searchConfiguration;
    private final FragmentManager fragmentManager;
    private final FragmentFactory fragmentFactory;

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                     final FragmentManager fragmentManager,
                                     final FragmentFactory fragmentFactory) {
        this.searchConfiguration = searchConfiguration;
        this.fragmentManager = fragmentManager;
        this.fragmentFactory = fragmentFactory;
    }

    public void showSearchPreferenceFragment() {
        Navigation.show(
                SearchPreferenceFragment.newInstance(searchConfiguration, fragmentFactory),
                true,
                fragmentManager,
                searchConfiguration.fragmentContainerViewId,
                searchPreferenceFragment -> {});
    }
}
