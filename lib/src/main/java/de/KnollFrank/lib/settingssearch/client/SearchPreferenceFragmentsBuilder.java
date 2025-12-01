package de.KnollFrank.lib.settingssearch.client;

import android.os.PersistableBundle;

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
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

public class SearchPreferenceFragmentsBuilder<C> {

    private final SearchDatabaseConfig searchDatabaseConfig;
    private final Locale locale;
    private final OnUiThreadRunner onUiThreadRunner;
    private final FragmentActivity activity;
    private final DAOProvider<C> daoProvider;
    private final SearchConfig searchConfig;
    private final PersistableBundle configuration;
    private final ConfigurationBundleConverter<C> configurationBundleConverter;
    private Supplier<Optional<AsyncTaskWithProgressUpdateListeners<Void, DAOProvider<C>>>> createSearchDatabaseTaskSupplier = Optional::empty;
    private Consumer<MergedPreferenceScreen<C>> onMergedPreferenceScreenAvailable = mergedPreferenceScreen -> {
    };


    protected SearchPreferenceFragmentsBuilder(final SearchDatabaseConfig searchDatabaseConfig,
                                               final SearchConfig searchConfig,
                                               final Locale locale,
                                               final OnUiThreadRunner onUiThreadRunner,
                                               final FragmentActivity activity,
                                               final DAOProvider<C> daoProvider,
                                               final PersistableBundle configuration,
                                               final ConfigurationBundleConverter<C> configurationBundleConverter) {
        this.searchDatabaseConfig = searchDatabaseConfig;
        this.searchConfig = searchConfig;
        this.locale = locale;
        this.onUiThreadRunner = onUiThreadRunner;
        this.activity = activity;
        this.daoProvider = daoProvider;
        this.configuration = configuration;
        this.configurationBundleConverter = configurationBundleConverter;
    }

    public SearchPreferenceFragmentsBuilder<C> withCreateSearchDatabaseTaskSupplier(final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<Void, DAOProvider<C>>>> createSearchDatabaseTaskSupplier) {
        this.createSearchDatabaseTaskSupplier = createSearchDatabaseTaskSupplier;
        return this;
    }

    public SearchPreferenceFragmentsBuilder<C> withOnMergedPreferenceScreenAvailable(final Consumer<MergedPreferenceScreen<C>> onMergedPreferenceScreenAvailable) {
        this.onMergedPreferenceScreenAvailable = onMergedPreferenceScreenAvailable;
        return this;
    }

    public SearchPreferenceFragments<C> build() {
        return new SearchPreferenceFragments<>(
                searchDatabaseConfig,
                searchConfig,
                locale,
                onUiThreadRunner,
                activity,
                createSearchDatabaseTaskSupplier,
                onMergedPreferenceScreenAvailable,
                daoProvider,
                configuration,
                configurationBundleConverter);
    }
}