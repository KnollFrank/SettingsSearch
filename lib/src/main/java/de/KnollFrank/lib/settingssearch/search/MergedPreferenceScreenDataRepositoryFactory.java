package de.KnollFrank.lib.settingssearch.search;

import androidx.fragment.app.FragmentActivity;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class MergedPreferenceScreenDataRepositoryFactory {

    public static <C> MergedPreferenceScreenDataRepository<C> createMergedPreferenceScreenDataRepository(
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final PreferenceDialogs preferenceDialogs,
            final SearchDatabaseConfig searchDatabaseConfig,
            final ProgressUpdateListener progressUpdateListener,
            final FragmentActivity activityContext,
            final DAOProvider<C> daoProvider,
            final Locale locale,
            final ConfigurationBundleConverter<C> configurationBundleConverter) {
        return new MergedPreferenceScreenDataRepository<C>(
                new PreferenceScreenWithHostProvider(
                        instantiateAndInitializeFragment,
                        searchDatabaseConfig.principalAndProxyProvider),
                preferenceDialogs,
                searchDatabaseConfig,
                progressUpdateListener,
                activityContext,
                daoProvider,
                locale,
                configurationBundleConverter);
    }
}
