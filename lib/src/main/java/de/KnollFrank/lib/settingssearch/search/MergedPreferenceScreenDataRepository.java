package de.KnollFrank.lib.settingssearch.search;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.LockingSupport;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverterFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.db.SearchablePreferenceScreenGraphRepository;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.settingssearch.graph.GraphToPojoGraphTransformer;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreenGraphListener;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreenGraphProviderFactory;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressProvider;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class MergedPreferenceScreenDataRepository<C> {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceDialogs preferenceDialogs;
    private final SearchDatabaseConfig searchDatabaseConfig;
    private final ProgressUpdateListener progressUpdateListener;
    private final FragmentActivity activityContext;
    private final PreferencesDatabase<C> preferencesDatabase;
    private final Locale locale;
    private final ConfigurationBundleConverter<C> configurationBundleConverter;

    MergedPreferenceScreenDataRepository(
            final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
            final PreferenceDialogs preferenceDialogs,
            final SearchDatabaseConfig searchDatabaseConfig,
            final ProgressUpdateListener progressUpdateListener,
            final FragmentActivity activityContext,
            final PreferencesDatabase<C> preferencesDatabase,
            final Locale locale,
            final ConfigurationBundleConverter<C> configurationBundleConverter) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceDialogs = preferenceDialogs;
        this.searchDatabaseConfig = searchDatabaseConfig;
        this.progressUpdateListener = progressUpdateListener;
        this.activityContext = activityContext;
        this.preferencesDatabase = preferencesDatabase;
        this.locale = locale;
        this.configurationBundleConverter = configurationBundleConverter;
    }

    public void fillSearchDatabaseWithPreferences(final Locale locale, final PersistableBundle configuration) {
        synchronized (LockingSupport.searchDatabaseLock) {
            final SearchablePreferenceScreenGraphRepository<C> graphRepository = preferencesDatabase.searchablePreferenceScreenGraphRepository();
            if (graphRepository
                    .findGraphById(locale, configurationBundleConverter.convertBackward(configuration), activityContext)
                    .isEmpty()) {
                // FK-TODO: show progressBar only for computePreferences() and not for load()?
                final var searchablePreferenceScreenGraph = computeSearchablePreferenceScreenGraph();
                progressUpdateListener.onProgressUpdate("persisting search database");
                graphRepository.persistOrReplace(
                        new SearchablePreferenceScreenGraph(
                                searchablePreferenceScreenGraph,
                                locale,
                                configuration));
            }
        }
    }

    private Tree<SearchablePreferenceScreen, SearchablePreference> computeSearchablePreferenceScreenGraph() {
        final Tree<SearchablePreferenceScreen, SearchablePreference> searchablePreferenceScreenGraph =
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
                new GraphToPojoGraphTransformer(
                        new PreferenceScreenToSearchablePreferenceScreenConverter(
                                Preference2SearchablePreferenceConverterFactory.createPreference2SearchablePreferenceConverter(
                                        searchDatabaseConfig,
                                        preferenceDialogs)),
                        searchDatabaseConfig.preferenceFragmentIdProvider),
                PreferenceScreenGraphProviderFactory.createPreferenceScreenGraphProvider(
                        preferenceScreenWithHostProvider,
                        searchDatabaseConfig.preferenceFragmentConnected2PreferenceProvider,
                        searchDatabaseConfig.rootPreferenceFragmentOfActivityProvider,
                        (edge, sourceNodeOfEdge, targetNodeOfEdge) -> true,
                        activityContext,
                        new PreferenceScreenGraphListener() {

                            @Override
                            public void preferenceScreenWithHostAdded(final PreferenceScreenWithHost preferenceScreenWithHost) {
                                progressUpdateListener.onProgressUpdate(ProgressProvider.getProgress(preferenceScreenWithHost));
                            }
                        }),
                locale);
    }
}
