package de.KnollFrank.settingssearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabase;

public class SearchPreferenceFragmentsFactory {

    private SearchPreferenceFragmentsFactory() {
    }

    public static SearchPreferenceFragments<Configuration> createSearchPreferenceFragments(
            final @IdRes int fragmentContainerViewId,
            final FragmentActivity activity,
            final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<Void, PreferencesDatabase<Configuration>>>> createSearchDatabaseTaskSupplier,
            final Consumer<MergedPreferenceScreen<Configuration>> onMergedPreferenceScreenAvailable,
            final PreferencesDatabase<Configuration> preferencesDatabase,
            final Configuration configuration,
            final SearchDatabaseConfig searchDatabaseConfig) {
        return SearchPreferenceFragments
                .builder(
                        searchDatabaseConfig,
                        SearchConfigFactory.createSearchConfig(fragmentContainerViewId, activity),
                        activity,
                        preferencesDatabase,
                        new ConfigurationBundleConverter().convertForward(configuration),
                        new ConfigurationBundleConverter())
                .withCreateSearchDatabaseTaskSupplier(createSearchDatabaseTaskSupplier)
                .withOnMergedPreferenceScreenAvailable(onMergedPreferenceScreenAvailable)
                .build();
    }
}
