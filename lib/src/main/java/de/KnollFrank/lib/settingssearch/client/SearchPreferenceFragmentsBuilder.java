package de.KnollFrank.lib.settingssearch.client;

import android.content.Context;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.search.ReflectionIconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.IconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.search.ui.ProgressContainerUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchPreferenceFragmentUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;

public class SearchPreferenceFragmentsBuilder {

    private final SearchConfiguration searchConfiguration;
    private final FragmentManager fragmentManager;
    private final Locale locale;
    private final OnUiThreadRunner onUiThreadRunner;
    private final Context context;
    private FragmentFactory fragmentFactory = new DefaultFragmentFactory();
    private SearchableInfoProvider searchableInfoProvider = preference -> Optional.empty();
    private PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider = (preference, hostOfPreference) -> Optional.empty();
    private IconResourceIdProvider iconResourceIdProvider = new ReflectionIconResourceIdProvider();
    private PreferenceSearchablePredicate preferenceSearchablePredicate = (preference, hostOfPreference) -> true;
    private IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate = (preference, hostOfPreference) -> true;
    private PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener = preferenceScreenGraph -> {
    };
    private ShowPreferencePathPredicate showPreferencePathPredicate = preferencePath -> preferencePath.getPreference().isPresent();
    private PrepareShow prepareShow = preferenceFragment -> {
    };
    private PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider = (preference, hostOfPreference) -> Optional.empty();
    private Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier = Optional::empty;
    private SearchPreferenceFragmentUI searchPreferenceFragmentUI =
            new SearchPreferenceFragmentUI() {

                @Override
                public @LayoutRes int getRootViewId() {
                    return R.layout.searchpreference_fragment;
                }

                @Override
                public SearchView getSearchView(final View rootView) {
                    return rootView.findViewById(R.id.searchView);
                }

                @Override
                public FragmentContainerView getSearchResultsFragmentContainerView(final View rootView) {
                    return rootView.findViewById(R.id.searchResultsFragmentContainerView);
                }

                @Override
                public ProgressContainerUI getProgressContainerUI(final View rootView) {
                    return new ProgressContainerUI() {

                        @Override
                        public View getRoot() {
                            return rootView.findViewById(R.id.progressContainer);
                        }

                        @Override
                        public TextView getProgressText() {
                            return getRoot().findViewById(R.id.progressText);
                        }
                    };
                }
            };
    private SearchResultsFragmentUI searchResultsFragmentUI =
            new SearchResultsFragmentUI() {

                @Override
                public int getRootViewId() {
                    return R.layout.searchresults_fragment;
                }

                @Override
                public RecyclerView getSearchResultsView(View rootView) {
                    return rootView.findViewById(R.id.searchResults);
                }
            };

    protected SearchPreferenceFragmentsBuilder(final SearchConfiguration searchConfiguration,
                                               final FragmentManager fragmentManager,
                                               final Locale locale,
                                               final OnUiThreadRunner onUiThreadRunner,
                                               final Context context) {
        this.searchConfiguration = searchConfiguration;
        this.fragmentManager = fragmentManager;
        this.locale = locale;
        this.onUiThreadRunner = onUiThreadRunner;
        this.context = context;
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

    public SearchPreferenceFragmentsBuilder withPreferenceFragmentConnected2PreferenceProvider(final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider) {
        this.preferenceFragmentConnected2PreferenceProvider = preferenceFragmentConnected2PreferenceProvider;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withCreateSearchDatabaseTaskSupplier(final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier) {
        this.createSearchDatabaseTaskSupplier = createSearchDatabaseTaskSupplier;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withSearchPreferenceFragmentUI(final SearchPreferenceFragmentUI searchPreferenceFragmentUI) {
        this.searchPreferenceFragmentUI = searchPreferenceFragmentUI;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withSearchResultsFragmentUI(final SearchResultsFragmentUI searchResultsFragmentUI) {
        this.searchResultsFragmentUI = searchResultsFragmentUI;
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
                preferenceFragmentConnected2PreferenceProvider,
                locale,
                onUiThreadRunner,
                context,
                createSearchDatabaseTaskSupplier,
                searchPreferenceFragmentUI,
                searchResultsFragmentUI);
    }
}