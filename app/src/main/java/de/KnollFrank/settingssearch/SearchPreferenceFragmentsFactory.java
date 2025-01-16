package de.KnollFrank.settingssearch;

import android.app.Activity;

import androidx.fragment.app.FragmentManager;

import java.util.Optional;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;

public class SearchPreferenceFragmentsFactory {

    public static SearchPreferenceFragments createSearchPreferenceFragments(
            final SearchConfiguration searchConfiguration,
            final FragmentManager fragmentManager,
            final Activity activity,
            final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier) {
        return SearchPreferenceFragments
                .builder(
                        searchConfiguration,
                        fragmentManager,
                        activity)
                .withSearchDatabaseConfig(SearchDatabaseConfigFactory.createSearchDatabaseConfig())
                .withSearchConfig(SearchConfigFactory.createSearchConfig(activity))
                .withCreateSearchDatabaseTaskSupplier(createSearchDatabaseTaskSupplier)
                .build();
    }
}
