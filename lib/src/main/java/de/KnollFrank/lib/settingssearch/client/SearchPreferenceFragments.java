package de.KnollFrank.lib.settingssearch.client;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.common.Utils;
import de.KnollFrank.lib.settingssearch.common.task.LongRunningTask;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenFactory;
import de.KnollFrank.lib.settingssearch.search.SearchDatabaseDirectoryIO;
import de.KnollFrank.lib.settingssearch.search.SearchPreferenceFragment;
import de.KnollFrank.lib.settingssearch.search.provider.BuiltinSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.search.provider.IconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchPreferenceFragments {

    public final SearchConfiguration searchConfiguration;
    public final FragmentFactory fragmentFactory;
    public final SearchableInfoProvider searchableInfoProvider;
    public final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    public final IconResourceIdProvider iconResourceIdProvider;
    public final PreferenceSearchablePredicate preferenceSearchablePredicate;
    private final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate;
    public final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final PrepareShow prepareShow;
    private final FragmentManager fragmentManager;
    public final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final Locale locale;
    private final OnUiThreadRunner onUiThreadRunner;
    private final Context context;
    private final Supplier<Optional<LongRunningTask<MergedPreferenceScreenData>>> taskSupplier;

    public static SearchPreferenceFragmentsBuilder builder(final SearchConfiguration searchConfiguration,
                                                           final FragmentManager fragmentManager,
                                                           final Context context,
                                                           final OnUiThreadRunner onUiThreadRunner) {
        return new SearchPreferenceFragmentsBuilder(
                searchConfiguration,
                fragmentManager,
                Utils.geCurrentLocale(context.getResources()),
                onUiThreadRunner,
                context);
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
                                        final Locale locale,
                                        final OnUiThreadRunner onUiThreadRunner,
                                        final Context context,
                                        final Supplier<Optional<LongRunningTask<MergedPreferenceScreenData>>> taskSupplier) {
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
        this.locale = locale;
        this.onUiThreadRunner = onUiThreadRunner;
        this.context = context;
        this.taskSupplier = taskSupplier;
    }

    public void showSearchPreferenceFragment() {
        showFragment(
                new SearchPreferenceFragment(
                        searchConfiguration,
                        includePreferenceInSearchResultsPredicate,
                        new MergedPreferenceScreenFactory(
                                showPreferencePathPredicate,
                                prepareShow,
                                searchConfiguration.fragmentContainerViewId(),
                                searchConfiguration.rootPreferenceFragment(),
                                fragmentFactory,
                                preferenceSearchablePredicate,
                                preferenceConnected2PreferenceFragmentProvider,
                                preferenceScreenGraphAvailableListener,
                                searchableInfoProvider,
                                preferenceDialogAndSearchableInfoProvider,
                                iconResourceIdProvider,
                                context,
                                locale,
                                onUiThreadRunner),
                        onUiThreadRunner,
                        taskSupplier),
                searchPreferenceFragment -> {
                },
                true,
                searchConfiguration.fragmentContainerViewId(),
                fragmentManager);
    }

    public void rebuildSearchDatabase() {
        new SearchDatabaseDirectoryIO(context).removeSearchDatabaseDirectories4AllLocales();
    }
}
