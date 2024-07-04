package de.KnollFrank.lib.preferencesearch.client;

import static de.KnollFrank.lib.preferencesearch.fragment.Fragments.showFragment;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceSummaryProvider.createBuiltinSummaryResetterFactories;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceSummaryProvider.createBuiltinSummarySetters;
import static de.KnollFrank.lib.preferencesearch.search.provider.BuiltinSearchableInfoProvidersFactory.createBuiltinSearchableInfoProviders;
import static de.KnollFrank.lib.preferencesearch.search.provider.CustomPreferenceDescriptions.getSearchableInfoProviders;
import static de.KnollFrank.lib.preferencesearch.search.provider.CustomPreferenceDescriptions.getSummaryResetterFactories;
import static de.KnollFrank.lib.preferencesearch.search.provider.CustomPreferenceDescriptions.getSummarySetters;

import androidx.fragment.app.FragmentManager;

import java.util.List;

import de.KnollFrank.lib.preferencesearch.fragment.FragmentFactory;
import de.KnollFrank.lib.preferencesearch.provider.SearchablePreferencePredicate;
import de.KnollFrank.lib.preferencesearch.search.SearchPreferenceFragment;
import de.KnollFrank.lib.preferencesearch.search.provider.CustomPreferenceDescription;
import de.KnollFrank.lib.preferencesearch.search.provider.ISearchableInfoProviderInternal;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoProviderInternal;
import de.KnollFrank.lib.preferencesearch.search.provider.SummaryResetterFactories;
import de.KnollFrank.lib.preferencesearch.search.provider.SummarySetter;

public class SearchPreferenceFragments {

    private final SearchConfiguration searchConfiguration;
    private final SearchablePreferencePredicate searchablePreferencePredicate;
    private final ISearchableInfoProviderInternal searchableInfoProviderInternal;
    private final SummarySetter summarySetter;
    private final SummaryResetterFactories summaryResetterFactories;
    private final FragmentFactory fragmentFactory;
    private final FragmentManager fragmentManager;

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                     final SearchablePreferencePredicate searchablePreferencePredicate,
                                     final List<CustomPreferenceDescription> customPreferenceDescriptions,
                                     final FragmentFactory fragmentFactory,
                                     final FragmentManager fragmentManager) {
        this.searchConfiguration = searchConfiguration;
        this.searchablePreferencePredicate = searchablePreferencePredicate;
        this.searchableInfoProviderInternal =
                new SearchableInfoProviderInternal(
                        createBuiltinSearchableInfoProviders().combineWith(
                                getSearchableInfoProviders(customPreferenceDescriptions)));
        this.summarySetter =
                new SummarySetter(
                        createBuiltinSummarySetters().combineWith(
                                getSummarySetters(customPreferenceDescriptions)));
        this.summaryResetterFactories =
                createBuiltinSummaryResetterFactories().combineWith(
                        getSummaryResetterFactories(customPreferenceDescriptions));
        this.fragmentFactory = fragmentFactory;
        this.fragmentManager = fragmentManager;
    }

    public void showSearchPreferenceFragment() {
        showFragment(
                SearchPreferenceFragment.newInstance(
                        searchConfiguration,
                        searchablePreferencePredicate,
                        searchableInfoProviderInternal,
                        summarySetter,
                        summaryResetterFactories,
                        fragmentFactory),
                searchPreferenceFragment -> {
                },
                true,
                searchConfiguration.fragmentContainerViewId,
                fragmentManager);
    }
}
