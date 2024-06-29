package de.KnollFrank.lib.preferencesearch.client;

import static de.KnollFrank.lib.preferencesearch.fragment.Fragments.showFragment;

import androidx.fragment.app.FragmentManager;

import de.KnollFrank.lib.preferencesearch.fragment.FragmentFactory;
import de.KnollFrank.lib.preferencesearch.search.SearchPreferenceFragment;
import de.KnollFrank.lib.preferencesearch.search.SearchableInfoProvider;

public class SearchPreferenceFragments {

    private final SearchConfiguration searchConfiguration;
    private final FragmentManager fragmentManager;
    private final SearchableInfoProvider searchableInfoProvider;
    private final FragmentFactory fragmentFactory;

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                     final FragmentManager fragmentManager,
                                     final SearchableInfoProvider searchableInfoProvider,
                                     final FragmentFactory fragmentFactory) {
        this.searchConfiguration = searchConfiguration;
        this.fragmentManager = fragmentManager;
        this.searchableInfoProvider = searchableInfoProvider;
        this.fragmentFactory = fragmentFactory;
    }

    public void showSearchPreferenceFragment() {
        showFragment(
                SearchPreferenceFragment.newInstance(searchConfiguration, searchableInfoProvider, fragmentFactory),
                searchPreferenceFragment -> {
                },
                true,
                searchConfiguration.fragmentContainerViewId,
                fragmentManager);
    }
}
