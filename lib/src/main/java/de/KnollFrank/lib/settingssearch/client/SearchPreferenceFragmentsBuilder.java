package de.KnollFrank.lib.settingssearch.client;

import androidx.fragment.app.FragmentManager;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataMode;
import de.KnollFrank.lib.settingssearch.search.ReflectionIconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.IconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchPreferenceFragmentsBuilder {

    private final SearchConfiguration searchConfiguration;
    private final FragmentManager fragmentManager;
    private final MergedPreferenceScreenDataMode mergedPreferenceScreenDataMode;
    private FragmentFactory fragmentFactory = new DefaultFragmentFactory();
    private SearchableInfoProvider searchableInfoProvider = preference -> Optional.empty();
    private PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider = (preference, hostOfPreference) -> Optional.empty();
    private IconResourceIdProvider iconResourceIdProvider = new ReflectionIconResourceIdProvider();
    private PreferenceSearchablePredicate preferenceSearchablePredicate = (preference, host) -> true;
    private IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate = (preference, host) -> true;
    private PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener = preferenceScreenGraph -> {
    };
    private ShowPreferencePathPredicate showPreferencePathPredicate = preferencePath -> preferencePath.getPreference().isPresent();
    private PrepareShow prepareShow = preferenceFragment -> {
    };
    private PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider = (preference, hostOfPreference) -> Optional.empty();

    protected SearchPreferenceFragmentsBuilder(final SearchConfiguration searchConfiguration,
                                               final FragmentManager fragmentManager,
                                               final MergedPreferenceScreenDataMode mergedPreferenceScreenDataMode) {
        this.searchConfiguration = searchConfiguration;
        this.fragmentManager = fragmentManager;
        this.mergedPreferenceScreenDataMode = mergedPreferenceScreenDataMode;
    }

    public SearchPreferenceFragmentsBuilder withFragmentFactory(final FragmentFactory fragmentFactory) {
        this.fragmentFactory = fragmentFactory;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withSearchableInfoProvider(final SearchableInfoProvider searchableInfoProvider) {
        this.searchableInfoProvider = searchableInfoProvider;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withPreferenceDialogAndSearchableInfoProvider(final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider) {
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        return this;
    }


    private SearchPreferenceFragmentsBuilder withIconResourceIdProvider(final IconResourceIdProvider iconResourceIdProvider) {
        this.iconResourceIdProvider = iconResourceIdProvider;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withPreferenceSearchablePredicate(final PreferenceSearchablePredicate preferenceSearchablePredicate) {
        this.preferenceSearchablePredicate = preferenceSearchablePredicate;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withIncludePreferenceInSearchResultsPredicate(final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate) {
        this.includePreferenceInSearchResultsPredicate = includePreferenceInSearchResultsPredicate;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withPreferenceScreenGraphAvailableListener(final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener) {
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withShowPreferencePathPredicate(final ShowPreferencePathPredicate showPreferencePathPredicate) {
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withPrepareShow(final PrepareShow prepareShow) {
        this.prepareShow = prepareShow;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withPreferenceConnected2PreferenceFragmentProvider(final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider) {
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        return this;
    }

    public SearchPreferenceFragments build() {
        return new SearchPreferenceFragments(
                searchConfiguration,
                fragmentFactory,
                searchableInfoProvider,
                preferenceDialogAndSearchableInfoProvider,
                iconResourceIdProvider,
                preferenceSearchablePredicate,
                includePreferenceInSearchResultsPredicate,
                preferenceScreenGraphAvailableListener,
                showPreferencePathPredicate,
                prepareShow,
                fragmentManager,
                preferenceConnected2PreferenceFragmentProvider,
                mergedPreferenceScreenDataMode);
    }
}