package de.KnollFrank.lib.settingssearch.client;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import android.content.res.Resources;

import androidx.fragment.app.FragmentManager;

import de.KnollFrank.lib.settingssearch.common.Utils;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenFactory;
import de.KnollFrank.lib.settingssearch.search.SearchPreferenceFragment;
import de.KnollFrank.lib.settingssearch.search.provider.BuiltinSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.search.provider.IconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchPreferenceFragments {

    public final SearchConfiguration searchConfiguration;
    private final FragmentFactory fragmentFactory;
    private final SearchableInfoProvider searchableInfoProvider;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    private final IconResourceIdProvider iconResourceIdProvider;
    private final PreferenceSearchablePredicate preferenceSearchablePredicate;
    private final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final PrepareShow prepareShow;
    private final FragmentManager fragmentManager;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final String language;

    public static SearchPreferenceFragmentsBuilder builder(final SearchConfiguration searchConfiguration,
                                                           final FragmentManager fragmentManager,
                                                           final Resources resources) {
        return new SearchPreferenceFragmentsBuilder(
                searchConfiguration,
                fragmentManager,
                Utils.geCurrentLanguage(resources));
    }

    protected SearchPreferenceFragments(final SearchConfiguration searchConfiguration,
                                        final FragmentFactory fragmentFactory,
                                        final SearchableInfoProvider searchableInfoProvider,
                                        final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                                        final IconResourceIdProvider iconResourceIdProvider,
                                        final PreferenceSearchablePredicate preferenceSearchablePredicate,
                                        final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate,
                                        final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                        final ShowPreferencePathPredicate showPreferencePathPredicate,
                                        final PrepareShow prepareShow,
                                        final FragmentManager fragmentManager,
                                        final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                        final String language) {
        this.searchConfiguration = searchConfiguration;
        this.fragmentFactory = fragmentFactory;
        this.searchableInfoProvider = searchableInfoProvider.orElse(new BuiltinSearchableInfoProvider());
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.iconResourceIdProvider = iconResourceIdProvider;
        this.preferenceSearchablePredicate = preferenceSearchablePredicate;
        this.includePreferenceInSearchResultsPredicate = includePreferenceInSearchResultsPredicate;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.prepareShow = prepareShow;
        this.fragmentManager = fragmentManager;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.language = language;
    }

    public void showSearchPreferenceFragment() {
        showFragment(
                new SearchPreferenceFragment(
                        searchConfiguration,
                        showPreferencePathPredicate,
                        includePreferenceInSearchResultsPredicate,
                        prepareShow,
                        new MergedPreferenceScreenFactory(
                                searchConfiguration.rootPreferenceFragment(),
                                fragmentFactory,
                                preferenceSearchablePredicate,
                                preferenceConnected2PreferenceFragmentProvider,
                                preferenceScreenGraphAvailableListener,
                                searchableInfoProvider,
                                preferenceDialogAndSearchableInfoProvider,
                                iconResourceIdProvider),
                        language),
                searchPreferenceFragment -> {
                },
                true,
                searchConfiguration.fragmentContainerViewId(),
                fragmentManager);
    }
}
