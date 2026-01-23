package de.KnollFrank.lib.settingssearch.search;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;

import com.google.common.graph.ImmutableValueGraph;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.LockingSupport;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverterFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.db.SearchablePreferenceScreenTreeRepository;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreenTreeBuilderFactory;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenTreeProvider;
import de.KnollFrank.lib.settingssearch.graph.TreeToPojoTreeTransformer;
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
            final SearchablePreferenceScreenTreeRepository<C> treeRepository = preferencesDatabase.searchablePreferenceScreenTreeRepository();
            if (treeRepository
                    .findTreeById(locale, configurationBundleConverter.convertBackward(configuration), activityContext)
                    .isEmpty()) {
                // FK-TODO: show progressBar only for computePreferences() and not for load()?
                final var searchablePreferenceScreenGraph = computeSearchablePreferenceScreenGraph();
                progressUpdateListener.onProgressUpdate("persisting search database");
                treeRepository.persistOrReplace(
                        new SearchablePreferenceScreenTree<>(
                                searchablePreferenceScreenGraph,
                                locale,
                                configuration));
            }
        }
    }

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    private Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> computeSearchablePreferenceScreenGraph() {
        final Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> searchablePreferenceScreenGraph =
                this
                        .getSearchablePreferenceScreenGraphProvider()
                        .getSearchablePreferenceScreenTree(
                                preferenceScreenWithHostProvider
                                        .getPreferenceScreenWithHostOfFragment(
                                                searchDatabaseConfig.rootPreferenceFragment,
                                                Optional.empty())
                                        .orElseThrow());
        progressUpdateListener.onProgressUpdate("preparing search database");
        return searchablePreferenceScreenGraph;
    }

    private SearchablePreferenceScreenTreeProvider getSearchablePreferenceScreenGraphProvider() {
        return new SearchablePreferenceScreenTreeProvider(
                new TreeToPojoTreeTransformer(
                        new PreferenceScreenToSearchablePreferenceScreenConverter(
                                Preference2SearchablePreferenceConverterFactory.createPreference2SearchablePreferenceConverter(
                                        searchDatabaseConfig,
                                        preferenceDialogs)),
                        searchDatabaseConfig.preferenceFragmentIdProvider),
                PreferenceScreenTreeBuilderFactory.createPreferenceScreenTreeBuilder(
                        preferenceScreenWithHostProvider,
                        searchDatabaseConfig.preferenceFragmentConnected2PreferenceProvider,
                        searchDatabaseConfig.rootPreferenceFragmentOfActivityProvider,
                        edge -> true,
                        new ProgressUpdatingTreeBuilderListener(searchDatabaseConfig.preferenceScreenTreeBuilderListener, progressUpdateListener),
                        activityContext),
                locale);
    }
}
