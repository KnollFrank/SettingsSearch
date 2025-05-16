package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PrincipalAndProxyProvider;
import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class MergedPreferenceScreenDataRepositoryFactory {

    public static MergedPreferenceScreenDataRepository createMergedPreferenceScreenDataRepository(
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final PreferenceDialogs preferenceDialogs,
            final SearchDatabaseConfig searchDatabaseConfig,
            final ProgressUpdateListener progressUpdateListener,
            final PrincipalAndProxyProvider principalAndProxyProvider,
            final Context context) {
        return new MergedPreferenceScreenDataRepository(
                createPreferenceScreenWithHostProvider(
                        instantiateAndInitializeFragment,
                        principalAndProxyProvider,
                        searchDatabaseConfig.preferenceSearchablePredicate),
                preferenceDialogs,
                searchDatabaseConfig,
                progressUpdateListener,
                context);
    }

    private static PreferenceScreenWithHostProvider createPreferenceScreenWithHostProvider(
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final PrincipalAndProxyProvider principalAndProxyProvider,
            final PreferenceSearchablePredicate preferenceSearchablePredicate) {
        return new PreferenceScreenWithHostProvider(
                instantiateAndInitializeFragment,
                new SearchablePreferenceScreenProvider(
                        new PreferenceVisibleAndSearchablePredicate(preferenceSearchablePredicate)),
                principalAndProxyProvider);
    }
}
