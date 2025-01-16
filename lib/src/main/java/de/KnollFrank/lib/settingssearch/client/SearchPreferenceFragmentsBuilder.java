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
    private SearchDatabaseConfig searchDatabaseConfig = new SearchDatabaseConfigBuilder().build();
    private SearchConfig searchConfig;
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
        this.searchConfig = new SearchConfigBuilder(context).build();
    }

    public SearchPreferenceFragmentsBuilder withSearchDatabaseConfig(final SearchDatabaseConfig searchDatabaseConfig) {
        this.searchDatabaseConfig = searchDatabaseConfig;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withSearchConfig(final SearchConfig searchConfig) {
        this.searchConfig = searchConfig;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withCreateSearchDatabaseTaskSupplier(final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier) {
        this.createSearchDatabaseTaskSupplier = createSearchDatabaseTaskSupplier;
        return this;
    }

    public SearchPreferenceFragments build() {
        return new SearchPreferenceFragments(
                searchConfiguration,
                searchDatabaseConfig,
                searchConfig,
                fragmentManager,
                locale,
                onUiThreadRunner,
                context,
                createSearchDatabaseTaskSupplier);
    }
}