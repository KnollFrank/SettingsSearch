package de.KnollFrank.lib.preferencesearch.client;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import de.KnollFrank.lib.preferencesearch.fragment.FragmentFactory;
import de.KnollFrank.lib.preferencesearch.fragment.navigation.Commit;
import de.KnollFrank.lib.preferencesearch.fragment.navigation.Navigation;
import de.KnollFrank.lib.preferencesearch.search.SearchPreferenceFragment;

public class SearchPreferenceFragments {

    private final SearchConfiguration searchConfiguration;
    private final FragmentManager fragmentManager;
    private final FragmentFactory fragmentFactory;

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                     final FragmentManager fragmentManager) {
        this(searchConfiguration, fragmentManager, Fragment::instantiate);
    }

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                     final FragmentManager fragmentManager,
                                     final FragmentFactory fragmentFactory) {
        this.searchConfiguration = searchConfiguration;
        this.fragmentManager = fragmentManager;
        this.fragmentFactory = fragmentFactory;
    }

    public void showSearchPreferenceFragment() {
        show(SearchPreferenceFragment.newInstance(this.searchConfiguration, fragmentFactory));
    }

    private void show(final Fragment fragment) {
        Navigation.show(
                fragment,
                true,
                this.fragmentManager,
                this.searchConfiguration.fragmentContainerViewId,
                Commit.COMMIT_ASYNC);
    }
}
