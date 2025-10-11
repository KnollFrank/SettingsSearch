package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import org.jgrapht.Graph;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.LockingSupport;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGeneratorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverterFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreen2SearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.settingssearch.graph.Graph2POJOGraphTransformer;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreenGraphListener;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreenGraphProviderFactory;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressProvider;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class MergedPreferenceScreenDataRepository {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceDialogs preferenceDialogs;
    private final SearchDatabaseConfig searchDatabaseConfig;
    private final ProgressUpdateListener progressUpdateListener;
    private final Context context;
    private final DAOProvider daoProvider;
    private final Locale locale;

    MergedPreferenceScreenDataRepository(
            final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
            final PreferenceDialogs preferenceDialogs,
            final SearchDatabaseConfig searchDatabaseConfig,
            final ProgressUpdateListener progressUpdateListener,
            final Context context,
            final DAOProvider daoProvider,
            final Locale locale) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceDialogs = preferenceDialogs;
        this.searchDatabaseConfig = searchDatabaseConfig;
        this.progressUpdateListener = progressUpdateListener;
        this.context = context;
        this.daoProvider = daoProvider;
        this.locale = locale;
    }

    public void fillSearchDatabaseWithPreferences(final Locale locale) {
        synchronized (LockingSupport.searchDatabaseLock) {
            final SearchablePreferenceScreenGraphDAO graphDAO = daoProvider.searchablePreferenceScreenGraphDAO();
            if (graphDAO.findGraphById(locale).isEmpty()) {
                // FK-TODO: show progressBar only for computePreferences() and not for load()?
                final var searchablePreferenceScreenGraph = computeSearchablePreferenceScreenGraph();
                progressUpdateListener.onProgressUpdate("persisting search database");
                graphDAO.persist(
                        new SearchablePreferenceScreenGraph(
                                searchablePreferenceScreenGraph,
                                locale,
                                false));
            }
        }
    }

    private Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> computeSearchablePreferenceScreenGraph() {
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> searchablePreferenceScreenGraph =
                this
                        .getSearchablePreferenceScreenGraphProvider()
                        .getSearchablePreferenceScreenGraph(
                                preferenceScreenWithHostProvider
                                        .getPreferenceScreenWithHostOfFragment(
                                                searchDatabaseConfig.rootPreferenceFragment,
                                                Optional.empty())
                                        .orElseThrow());
        progressUpdateListener.onProgressUpdate("preparing search database");
        return searchablePreferenceScreenGraph;
    }

    private SearchablePreferenceScreenGraphProvider getSearchablePreferenceScreenGraphProvider() {
        return new SearchablePreferenceScreenGraphProvider(
                searchDatabaseConfig.preferenceScreenGraphAvailableListener,
                searchDatabaseConfig.computePreferencesListener,
                new Graph2POJOGraphTransformer(
                        new PreferenceScreen2SearchablePreferenceScreenConverter(
                                Preference2SearchablePreferenceConverterFactory.createPreference2SearchablePreferenceConverter(
                                        searchDatabaseConfig,
                                        preferenceDialogs,
                                        IdGeneratorFactory.createIdGeneratorStartingAt(1))),
                        searchDatabaseConfig.preferenceFragmentIdProvider),
                PreferenceScreenGraphProviderFactory.createPreferenceScreenGraphProvider(
                        preferenceScreenWithHostProvider,
                        searchDatabaseConfig.preferenceFragmentConnected2PreferenceProvider,
                        searchDatabaseConfig.rootPreferenceFragmentOfActivityProvider,
                        context,
                        new PreferenceScreenGraphListener() {

                            @Override
                            public void preferenceScreenWithHostAdded(final PreferenceScreenWithHost preferenceScreenWithHost) {
                                progressUpdateListener.onProgressUpdate(ProgressProvider.getProgress(preferenceScreenWithHost));
                            }
                        }),
                locale);
    }
}
