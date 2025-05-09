package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import org.jgrapht.Graph;

import java.util.Locale;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PrincipalAndProxyProvider;
import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.LockingSupport;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGeneratorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverterFactory;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceDAO;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.settingssearch.graph.PojoGraphs;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreenGraphListener;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreenGraphProviderFactory;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressProvider;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class MergedPreferenceScreenDataRepository {

    private final InstantiateAndInitializeFragment instantiateAndInitializeFragment;
    private final PreferenceDialogs preferenceDialogs;
    private final SearchDatabaseConfig searchDatabaseConfig;
    private final ProgressUpdateListener progressUpdateListener;
    private final PrincipalAndProxyProvider principalAndProxyProvider;
    private final Context context;

    public MergedPreferenceScreenDataRepository(
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final PreferenceDialogs preferenceDialogs,
            final SearchDatabaseConfig searchDatabaseConfig,
            final ProgressUpdateListener progressUpdateListener,
            final PrincipalAndProxyProvider principalAndProxyProvider,
            final Context context) {
        this.instantiateAndInitializeFragment = instantiateAndInitializeFragment;
        this.preferenceDialogs = preferenceDialogs;
        this.searchDatabaseConfig = searchDatabaseConfig;
        this.progressUpdateListener = progressUpdateListener;
        this.principalAndProxyProvider = principalAndProxyProvider;
        this.context = context;
    }

    public SearchablePreferenceDAO getSearchDatabaseFilledWithPreferences(final Locale locale) {
        synchronized (LockingSupport.searchDatabaseLock) {
            final AppDatabase appDatabase = AppDatabaseFactory.getInstance(locale, context);
            final SearchablePreferenceDAO searchablePreferenceDAO = appDatabase.searchablePreferenceDAO();
            if (!appDatabase.searchDatabaseStateDAO().isSearchDatabaseInitialized()) {
                computeAndPersistPreferences(searchablePreferenceDAO);
                appDatabase.searchDatabaseStateDAO().setSearchDatabaseInitialized(true);
            }
            return searchablePreferenceDAO;
        }
    }

    private void computeAndPersistPreferences(final SearchablePreferenceDAO searchablePreferenceDAO) {
        // FK-TODO: show progressBar only for computePreferences() and not for load()?
        final Set<SearchablePreference> preferences = computePreferences();
        progressUpdateListener.onProgressUpdate("persisting search database");
        searchablePreferenceDAO.persist(preferences);
    }

    private Set<SearchablePreference> computePreferences() {
        // FK-TODO: return this graph and persist it using SearchablePreferenceScreenGraphDAO
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> searchablePreferenceScreenGraph =
                this
                        .getSearchablePreferenceScreenGraphProvider()
                        .getSearchablePreferenceScreenGraph();
        progressUpdateListener.onProgressUpdate("preparing search database");
        return PojoGraphs.getPreferences(searchablePreferenceScreenGraph);
    }

    private SearchablePreferenceScreenGraphProvider getSearchablePreferenceScreenGraphProvider() {
        return new SearchablePreferenceScreenGraphProvider(
                searchDatabaseConfig.rootPreferenceFragment,
                searchDatabaseConfig.preferenceScreenGraphAvailableListener,
                searchDatabaseConfig.computePreferencesListener,
                Preference2SearchablePreferenceConverterFactory.createPreference2SearchablePreferenceConverter(
                        searchDatabaseConfig,
                        preferenceDialogs,
                        IdGeneratorFactory.createIdGeneratorStartingAt(1)),
                PreferenceScreenGraphProviderFactory.createPreferenceScreenGraphProvider(
                        new PreferenceScreenWithHostProvider(
                                instantiateAndInitializeFragment,
                                new SearchablePreferenceScreenProvider(
                                        new PreferenceVisibleAndSearchablePredicate(
                                                searchDatabaseConfig.preferenceSearchablePredicate)),
                                principalAndProxyProvider),
                        searchDatabaseConfig.preferenceFragmentConnected2PreferenceProvider,
                        searchDatabaseConfig.rootPreferenceFragmentOfActivityProvider,
                        context,
                        new PreferenceScreenGraphListener() {

                            @Override
                            public void preferenceScreenWithHostAdded(final PreferenceScreenWithHost preferenceScreenWithHost) {
                                progressUpdateListener.onProgressUpdate(ProgressProvider.getProgress(preferenceScreenWithHost));
                            }
                        }));
    }
}
