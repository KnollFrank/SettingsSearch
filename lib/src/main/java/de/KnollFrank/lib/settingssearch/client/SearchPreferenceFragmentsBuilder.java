package de.KnollFrank.lib.settingssearch.client;

import androidx.fragment.app.FragmentActivity;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProvider;

public class SearchPreferenceFragmentsBuilder {

    private final SearchDatabaseConfig searchDatabaseConfig;
    private final Locale locale;
    private final OnUiThreadRunner onUiThreadRunner;
    private final FragmentActivity activity;
    private final SearchConfig searchConfig;
    private Supplier<Optional<AsyncTaskWithProgressUpdateListeners<Void, DAOProvider>>> createSearchDatabaseTaskSupplier = Optional::empty;
    private Consumer<MergedPreferenceScreen> onMergedPreferenceScreenAvailable = mergedPreferenceScreen -> {
    };


    protected SearchPreferenceFragmentsBuilder(final SearchDatabaseConfig searchDatabaseConfig,
                                               final SearchConfig searchConfig,
                                               final Locale locale,
                                               final OnUiThreadRunner onUiThreadRunner,
                                               final FragmentActivity activity) {
        this.searchDatabaseConfig = searchDatabaseConfig;
        this.searchConfig = searchConfig;
        this.locale = locale;
        this.onUiThreadRunner = onUiThreadRunner;
        this.activity = activity;
    }

    public SearchPreferenceFragmentsBuilder withCreateSearchDatabaseTaskSupplier(final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<Void, DAOProvider>>> createSearchDatabaseTaskSupplier) {
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
                locale,
                onUiThreadRunner,
                activity,
                createSearchDatabaseTaskSupplier,
                onMergedPreferenceScreenAvailable);
    }
}