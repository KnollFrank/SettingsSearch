package de.KnollFrank.lib.preferencesearch.client;

import static de.KnollFrank.lib.preferencesearch.fragment.Fragments.showFragment;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceSummaryProvider.combineSummaryResetterFactories;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceSummaryProvider.createBuiltinSummaryResetterFactories;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceSummaryProvider.createBuiltinSummarySetters;
import static de.KnollFrank.lib.preferencesearch.search.provider.BuiltinSearchableInfoProvidersFactory.createBuiltinSearchableInfoProviders;
import static de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoProviders.combineSearchableInfoProviders;
import static de.KnollFrank.lib.preferencesearch.search.provider.SummarySetters.combineSummarySetters;

import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;

import java.util.Map;

import de.KnollFrank.lib.preferencesearch.fragment.FragmentFactory;
import de.KnollFrank.lib.preferencesearch.provider.SearchablePreferencePredicate;
import de.KnollFrank.lib.preferencesearch.search.SearchPreferenceFragment;
import de.KnollFrank.lib.preferencesearch.search.provider.ISearchableInfoProviderInternal;
import de.KnollFrank.lib.preferencesearch.search.provider.ISummarySetter;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoProvider;
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
                                     final Map<Class<? extends Preference>, SearchableInfoProvider<?>> searchableInfoProviders,
                                     final Map<Class<? extends Preference>, ISummarySetter> summarySetterByPreferenceClass,
                                     final SummaryResetterFactories summaryResetterFactories,
                                     final FragmentFactory fragmentFactory,
                                     final FragmentManager fragmentManager) {
        this.searchConfiguration = searchConfiguration;
        this.searchablePreferencePredicate = searchablePreferencePredicate;
        this.searchableInfoProviderInternal =
                new SearchableInfoProviderInternal(
                        combineSearchableInfoProviders(
                                createBuiltinSearchableInfoProviders(),
                                searchableInfoProviders));
        this.summarySetter =
                new SummarySetter(
                        combineSummarySetters(
                                createBuiltinSummarySetters(),
                                summarySetterByPreferenceClass));
        this.summaryResetterFactories =
                combineSummaryResetterFactories(
                        createBuiltinSummaryResetterFactories(),
                        summaryResetterFactories);
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
