package de.KnollFrank.settingssearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;
import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProvider;

public class SearchPreferenceFragmentsFactory {

    public static SearchPreferenceFragments<Configuration> createSearchPreferenceFragments(
            final @IdRes int fragmentContainerViewId,
            final FragmentActivity activity,
            final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<Void, DAOProvider<Configuration>>>> createSearchDatabaseTaskSupplier,
            final Consumer<MergedPreferenceScreen<Configuration>> onMergedPreferenceScreenAvailable,
            final DAOProvider<Configuration> daoProvider,
            final Configuration configuration) {
        return SearchPreferenceFragments
                .builder(
                        SearchDatabaseConfigFactory.createSearchDatabaseConfig(),
                        SearchConfigFactory.createSearchConfig(fragmentContainerViewId, activity),
                        activity,
                        daoProvider,
                        new ConfigurationBundleConverter().convertForward(configuration),
                        new ConfigurationBundleConverter())
                .withCreateSearchDatabaseTaskSupplier(createSearchDatabaseTaskSupplier)
                .withOnMergedPreferenceScreenAvailable(onMergedPreferenceScreenAvailable)
                .build();
    }
}
