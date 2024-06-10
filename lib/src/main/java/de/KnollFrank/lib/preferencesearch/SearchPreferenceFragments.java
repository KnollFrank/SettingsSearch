package de.KnollFrank.lib.preferencesearch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SearchPreferenceFragments {

    private final SearchConfiguration searchConfiguration;

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration) {
        this.searchConfiguration = searchConfiguration;
    }

    public void showSearchPreferenceFragment() {
        show(SearchPreferenceFragment.newInstance(this.searchConfiguration));
    }

    private void show(final Fragment fragment) {
        this
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        this.searchConfiguration.fragmentContainerViewId,
                        fragment,
                        fragment.getClass().getName())
                .commit();
    }

    private FragmentManager getSupportFragmentManager() {
        return this.searchConfiguration.activity.get().getSupportFragmentManager();
    }
}
