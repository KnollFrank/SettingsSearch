package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProvider;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class MergedPreferenceScreenDataRepositoryFactory {

    public static MergedPreferenceScreenDataRepository createMergedPreferenceScreenDataRepository(
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final PreferenceDialogs preferenceDialogs,
            final SearchDatabaseConfig searchDatabaseConfig,
            final ProgressUpdateListener progressUpdateListener,
            final Context context,
            final DAOProvider daoProvider,
            final Locale locale) {
        return new MergedPreferenceScreenDataRepository(
                new PreferenceScreenWithHostProvider(
                        instantiateAndInitializeFragment,
                        searchDatabaseConfig.principalAndProxyProvider),
                preferenceDialogs,
                searchDatabaseConfig,
                progressUpdateListener,
                context,
                daoProvider,
                locale);
    }
}
