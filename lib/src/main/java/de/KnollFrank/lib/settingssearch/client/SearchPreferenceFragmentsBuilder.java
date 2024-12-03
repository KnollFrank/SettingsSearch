package de.KnollFrank.lib.settingssearch.client;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;

public class SearchPreferenceFragmentsBuilder {

    private final SearchConfiguration searchConfiguration;
    private final FragmentManager fragmentManager;
    private final Locale locale;
    private final OnUiThreadRunner onUiThreadRunner;
    private final Context context;
    private SearchDatabase searchDatabase = new SearchDatabaseBuilder().build();
    private Search search = new SearchBuilder().build();
    private Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier = Optional::empty;

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

    public SearchPreferenceFragmentsBuilder withSearchDatabase(final SearchDatabase searchDatabase) {
        this.searchDatabase = searchDatabase;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withSearch(final Search search) {
        this.search = search;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withCreateSearchDatabaseTaskSupplier(final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier) {
        this.createSearchDatabaseTaskSupplier = createSearchDatabaseTaskSupplier;
        return this;
    }

    public SearchPreferenceFragments build() {
        return new SearchPreferenceFragments(
                searchConfiguration,
                searchDatabase,
                search,
                fragmentManager,
                locale,
                onUiThreadRunner,
                context,
                createSearchDatabaseTaskSupplier);
    }
}