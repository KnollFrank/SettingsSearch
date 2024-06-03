package com.bytehamster.lib.preferencesearch;

import android.os.Bundle;

public class SearchPreferenceFragments {

    private final SearchConfiguration searchConfiguration;

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration) {
        this.searchConfiguration = searchConfiguration;
    }

    public SearchPreferenceFragment createAndShowSearchPreferenceFragment() {
        if (this.searchConfiguration.getActivity() == null) {
            throw new IllegalStateException("setActivity() not called");
        }
        final SearchPreferenceFragment searchPreferenceFragment = createSearchPreferenceFragment();
        show(searchPreferenceFragment);
        return searchPreferenceFragment;
    }

    private SearchPreferenceFragment createSearchPreferenceFragment() {
        final SearchPreferenceFragment searchPreferenceFragment = new SearchPreferenceFragment();
        final Bundle bundle = SearchConfigurations.toBundle(this.searchConfiguration);
        PreferenceItemsBundle.writePreferenceItems(
                bundle,
                PreferenceItems.getPreferenceItems(
                        this.searchConfiguration.getPreferenceFragments(),
                        PreferenceProviderFactory.createPreferenceProvider(
                                this.searchConfiguration.getActivity(),
                                this.searchConfiguration.getDummyFragmentContainerViewId())));
        searchPreferenceFragment.setArguments(bundle);
        return searchPreferenceFragment;
    }

    private void show(final SearchPreferenceFragment searchPreferenceFragment) {
        this
                .searchConfiguration
                .getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .add(
                        this.searchConfiguration.getFragmentContainerViewId(),
                        searchPreferenceFragment,
                        SearchPreferenceFragment.TAG)
                .addToBackStack(SearchPreferenceFragment.TAG)
                .commit();
    }
}
