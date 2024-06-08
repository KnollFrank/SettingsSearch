package de.KnollFrank.lib.preferencesearch;

import androidx.fragment.app.Fragment;

public class SearchPreferenceFragments {

    private final SearchConfiguration searchConfiguration;

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration) {
        this.searchConfiguration = searchConfiguration;
    }

    // FK-TODO: rename method
    public void showSearchPreferenceFragment2() {
        if (this.searchConfiguration.getActivity() == null) {
            throw new IllegalStateException("setActivity() not called");
        }
        show(createSearchPreferenceFragment2());
    }

    private SearchPreferenceFragment2 createSearchPreferenceFragment2() {
        final SearchPreferenceFragment2 searchPreferenceFragment2 = new SearchPreferenceFragment2();
        searchPreferenceFragment2.setArguments(SearchConfigurations.toBundle(this.searchConfiguration));
        return searchPreferenceFragment2;
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
