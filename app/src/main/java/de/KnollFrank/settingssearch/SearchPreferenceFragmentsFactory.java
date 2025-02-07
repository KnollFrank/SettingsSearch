package de.KnollFrank.settingssearch;

import android.app.Activity;

import androidx.fragment.app.FragmentManager;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.settingssearch.common.task.AsyncTaskWithProgressUpdateListeners;

public class SearchPreferenceFragmentsFactory {

    public static SearchPreferenceFragments createSearchPreferenceFragments(
            final SearchConfiguration searchConfiguration,
            final FragmentManager fragmentManager,
            final Activity activity,
            final Supplier<Optional<AsyncTaskWithProgressUpdateListeners<?>>> createSearchDatabaseTaskSupplier,
            final Consumer<MergedPreferenceScreen> onMergedPreferenceScreenAvailable) {
        return SearchPreferenceFragments
                .builder(
                        SearchDatabaseConfigFactory.createSearchDatabaseConfig(),
                        searchConfiguration,
                        fragmentManager,
                        activity)
                .withSearchConfig(SearchConfigFactory.createSearchConfig(activity))
                .withCreateSearchDatabaseTaskSupplier(createSearchDatabaseTaskSupplier)
                .withOnMergedPreferenceScreenAvailable(onMergedPreferenceScreenAvailable)
                .build();
    }
}
