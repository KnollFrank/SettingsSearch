package de.KnollFrank.lib.preferencesearch.client;

import static de.KnollFrank.lib.preferencesearch.fragment.Fragments.showFragment;
import static de.KnollFrank.lib.preferencesearch.search.provider.BuiltinSearchableInfoProvidersFactory.createBuiltinSearchableInfoProviders;
import static de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoProviders.combineSearchableInfoProviders;

import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;

import java.util.Map;
import java.util.function.Function;

import de.KnollFrank.lib.preferencesearch.fragment.FragmentFactory;
import de.KnollFrank.lib.preferencesearch.provider.SearchablePreferencePredicate;
import de.KnollFrank.lib.preferencesearch.search.SearchPreferenceFragment;
import de.KnollFrank.lib.preferencesearch.search.provider.ISearchableInfoProviderInternal;
import de.KnollFrank.lib.preferencesearch.search.provider.ISummaryResetter;
import de.KnollFrank.lib.preferencesearch.search.provider.ISummarySetter;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoProvider;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoProviderInternal;

public class SearchPreferenceFragments {

    private final SearchConfiguration searchConfiguration;
    private final SearchablePreferencePredicate searchablePreferencePredicate;
    private final ISearchableInfoProviderInternal searchableInfoProviderInternal;
    private final Map<Class<? extends Preference>, ISummarySetter> summarySetterByPreferenceClass;
    private final Map<Class<? extends Preference>, Function<Preference, ? extends ISummaryResetter>> summaryResetterFactoryByPreferenceClass;
    private final FragmentFactory fragmentFactory;
    private final FragmentManager fragmentManager;

    public SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                     final SearchablePreferencePredicate searchablePreferencePredicate,
                                     final Map<Class<? extends Preference>, SearchableInfoProvider<?>> searchableInfoProviders,
                                     final Map<Class<? extends Preference>, ISummarySetter> summarySetterByPreferenceClass,
                                     final Map<Class<? extends Preference>, Function<Preference, ? extends ISummaryResetter>> summaryResetterFactoryByPreferenceClass,
                                     final FragmentFactory fragmentFactory,
                                     final FragmentManager fragmentManager) {
        this.searchConfiguration = searchConfiguration;
        this.searchablePreferencePredicate = searchablePreferencePredicate;
        this.searchableInfoProviderInternal =
                new SearchableInfoProviderInternal(
                        combineSearchableInfoProviders(
                                createBuiltinSearchableInfoProviders(),
                                searchableInfoProviders));
        this.summarySetterByPreferenceClass = summarySetterByPreferenceClass;
        this.summaryResetterFactoryByPreferenceClass = summaryResetterFactoryByPreferenceClass;
        this.fragmentFactory = fragmentFactory;
        this.fragmentManager = fragmentManager;
    }

    public void showSearchPreferenceFragment() {
        showFragment(
                SearchPreferenceFragment.newInstance(
                        searchConfiguration,
                        searchablePreferencePredicate,
                        searchableInfoProviderInternal,
                        summarySetterByPreferenceClass,
                        summaryResetterFactoryByPreferenceClass,
                        fragmentFactory),
                searchPreferenceFragment -> {
                },
                true,
                searchConfiguration.fragmentContainerViewId,
                fragmentManager);
    }
}
