package com.bytehamster.lib.preferencesearch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class SearchPreferenceFragments {

    private final SearchConfiguration searchConfiguration;

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration) {
        this.searchConfiguration = searchConfiguration;
    }

    public void showSearchPreferenceFragment() {
        if (this.searchConfiguration.getActivity() == null) {
            throw new IllegalStateException("setActivity() not called");
        }
        show(createSearchPreferenceFragment());
    }

    // FK-TODO: rename method
    public void showSearchPreferenceFragment2() {
        if (this.searchConfiguration.getActivity() == null) {
            throw new IllegalStateException("setActivity() not called");
        }
        show(new SearchPreferenceFragment2());
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
                                this.searchConfiguration.getActivity().getSupportFragmentManager(),
                                this.searchConfiguration.getDummyFragmentContainerViewId())));
        searchPreferenceFragment.setArguments(bundle);
        return searchPreferenceFragment;
    }

    private void show(final Fragment fragment) {
        this
                .searchConfiguration
                .getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .add(
                        this.searchConfiguration.getFragmentContainerViewId(),
                        fragment,
                        fragment.getClass().getName())
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }
}
