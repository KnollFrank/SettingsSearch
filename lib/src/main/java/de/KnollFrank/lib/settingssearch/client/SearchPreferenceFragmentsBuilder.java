package de.KnollFrank.lib.settingssearch.client;

import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import java.util.Optional;
import java.util.function.BiFunction;

import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePath;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchPreferenceFragmentsBuilder {

    private final SearchConfiguration searchConfiguration;
    private final FragmentManager fragmentManager;
    private FragmentFactory fragmentFactory = new DefaultFragmentFactory();
    private SearchableInfoProvider searchableInfoProvider = preference -> Optional.empty();
    private PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider = (preference, hostOfPreference) -> Optional.empty();
    private IsPreferenceSearchable isPreferenceSearchable = (preference, host) -> true;
    private PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener = preferenceScreenGraph -> {
    };
    private ShowPreferencePath showPreferencePath = preferencePath -> preferencePath.getPreference().isPresent();
    private PrepareShow prepareShow = preferenceFragment -> {
    };
    private PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider = (preference, hostOfPreference) -> Optional.empty();
    private BiFunction<SearchablePreferenceScreenGraphProvider, PreferenceManager, SearchablePreferenceScreenGraphProvider> wrapSearchablePreferenceScreenGraphProvider =
            (searchablePreferenceScreenGraphProvider, preferenceManager) ->
                    searchablePreferenceScreenGraphProvider;

    protected SearchPreferenceFragmentsBuilder(final SearchConfiguration searchConfiguration,
                                               final FragmentManager fragmentManager) {
        this.searchConfiguration = searchConfiguration;
        this.fragmentManager = fragmentManager;
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

    public SearchPreferenceFragmentsBuilder withIsPreferenceSearchable(final IsPreferenceSearchable isPreferenceSearchable) {
        this.isPreferenceSearchable = isPreferenceSearchable;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withPreferenceScreenGraphAvailableListener(final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener) {
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withShowPreferencePath(final ShowPreferencePath showPreferencePath) {
        this.showPreferencePath = showPreferencePath;
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

    public SearchPreferenceFragmentsBuilder withWrapSearchablePreferenceScreenGraphProvider(final BiFunction<SearchablePreferenceScreenGraphProvider, PreferenceManager, SearchablePreferenceScreenGraphProvider> wrapSearchablePreferenceScreenGraphProvider) {
        this.wrapSearchablePreferenceScreenGraphProvider = wrapSearchablePreferenceScreenGraphProvider;
        return this;
    }

    public SearchPreferenceFragments build() {
        return new SearchPreferenceFragments(
                searchConfiguration,
                fragmentFactory,
                searchableInfoProvider,
                preferenceDialogAndSearchableInfoProvider,
                isPreferenceSearchable,
                preferenceScreenGraphAvailableListener,
                showPreferencePath,
                prepareShow,
                fragmentManager,
                preferenceConnected2PreferenceFragmentProvider,
                wrapSearchablePreferenceScreenGraphProvider);
    }
}