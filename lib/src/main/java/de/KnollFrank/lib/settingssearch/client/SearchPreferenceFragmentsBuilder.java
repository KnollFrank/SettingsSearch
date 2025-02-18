package de.KnollFrank.lib.settingssearch.client;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;

public class SearchPreferenceFragmentsBuilder {

    private final SearchDatabaseConfig searchDatabaseConfig;
    private final FragmentManager fragmentManager;
    private final Locale locale;
    private final OnUiThreadRunner onUiThreadRunner;
    private final Context context;
    private final SearchConfig searchConfig;
    private Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier = Optional::empty;
    private Consumer<MergedPreferenceScreen> onMergedPreferenceScreenAvailable = mergedPreferenceScreen -> {
    };

    protected SearchPreferenceFragmentsBuilder(final SearchDatabaseConfig searchDatabaseConfig,
                                               final SearchConfig searchConfig,
                                               final FragmentManager fragmentManager,
                                               final Locale locale,
                                               final OnUiThreadRunner onUiThreadRunner,
                                               final Context context) {
        this.searchDatabaseConfig = searchDatabaseConfig;
        this.searchConfig = searchConfig;
        this.fragmentManager = fragmentManager;
        this.locale = locale;
        this.onUiThreadRunner = onUiThreadRunner;
        this.context = context;
    }

    public SearchPreferenceFragmentsBuilder withCreateSearchDatabaseTaskSupplier(final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier) {
        this.createSearchDatabaseTaskSupplier = createSearchDatabaseTaskSupplier;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withOnMergedPreferenceScreenAvailable(final Consumer<MergedPreferenceScreen> onMergedPreferenceScreenAvailable) {
        this.onMergedPreferenceScreenAvailable = onMergedPreferenceScreenAvailable;
        return this;
    }

    public SearchPreferenceFragments build() {
        return new SearchPreferenceFragments(
                searchDatabaseConfig,
                searchConfig,
                fragmentManager,
                locale,
                onUiThreadRunner,
                context,
                createSearchDatabaseTaskSupplier,
                onMergedPreferenceScreenAvailable);
    }
}