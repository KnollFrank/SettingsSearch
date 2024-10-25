package de.KnollFrank.lib.settingssearch.client;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import androidx.fragment.app.FragmentManager;

import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProviderWrapper;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.search.SearchPreferenceFragment;
import de.KnollFrank.lib.settingssearch.search.provider.BuiltinSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchPreferenceFragments {

    public final SearchConfiguration searchConfiguration;
    private final FragmentFactory fragmentFactory;
    private final SearchableInfoProvider searchableInfoProvider;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    private final IsPreferenceSearchable isPreferenceSearchable;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final PrepareShow prepareShow;
    private final FragmentManager fragmentManager;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final SearchablePreferenceScreenGraphProviderWrapper searchablePreferenceScreenGraphProviderWrapper;

    public static SearchPreferenceFragmentsBuilder builder(final SearchConfiguration searchConfiguration,
                                                           final FragmentManager fragmentManager) {
        return new SearchPreferenceFragmentsBuilder(searchConfiguration, fragmentManager);
    }

    protected SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                        final FragmentFactory fragmentFactory,
                                        final SearchableInfoProvider searchableInfoProvider,
                                        final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                                        final IsPreferenceSearchable isPreferenceSearchable,
                                        final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                        final ShowPreferencePathPredicate showPreferencePathPredicate,
                                        final PrepareShow prepareShow,
                                        final FragmentManager fragmentManager,
                                        final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                        final SearchablePreferenceScreenGraphProviderWrapper searchablePreferenceScreenGraphProviderWrapper) {
        this.searchConfiguration = searchConfiguration;
        this.fragmentFactory = fragmentFactory;
        this.searchableInfoProvider = searchableInfoProvider.orElse(new BuiltinSearchableInfoProvider());
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.prepareShow = prepareShow;
        this.fragmentManager = fragmentManager;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.searchablePreferenceScreenGraphProviderWrapper = searchablePreferenceScreenGraphProviderWrapper;
    }

    public void showSearchPreferenceFragment() {
        showFragment(
                new SearchPreferenceFragment(
                        searchConfiguration,
                        isPreferenceSearchable,
                        searchableInfoProvider,
                        new SearchableInfoAttribute(),
                        showPreferencePathPredicate,
                        fragmentFactory,
                        preferenceDialogAndSearchableInfoProvider,
                        preferenceScreenGraphAvailableListener,
                        prepareShow,
                        preferenceConnected2PreferenceFragmentProvider,
                        searchablePreferenceScreenGraphProviderWrapper),
                searchPreferenceFragment -> {
                },
                true,
                searchConfiguration.fragmentContainerViewId(),
                fragmentManager);
    }
}
