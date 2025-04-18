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
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchDatabaseDirectoryIO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceDAO;
import de.KnollFrank.lib.settingssearch.db.preference.db.FileDatabaseFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenDataFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreenGraphListener;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.provider.SearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressProvider;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class MergedPreferenceScreenDataRepository {

    private final InstantiateAndInitializeFragment instantiateAndInitializeFragment;
    private final DefaultFragmentInitializer preferenceDialogs;
    private final SearchDatabaseConfig searchDatabaseConfig;
    private final ProgressUpdateListener progressUpdateListener;
    private final PrincipalAndProxyProvider principalAndProxyProvider;
    private final Context context;

    public MergedPreferenceScreenDataRepository(
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final DefaultFragmentInitializer preferenceDialogs,
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
            final SearchablePreferenceDAO searchablePreferenceDAO = getSearchablePreferenceDAO(locale);
            if (!searchablePreferenceDAO.isDatabaseInitialized()) {
                computeAndPersistPreferences(searchablePreferenceDAO);
            }
            return searchablePreferenceDAO;
        }
    }

    private SearchablePreferenceDAO getSearchablePreferenceDAO(final Locale locale) {
        final FileDatabaseFactory fileDatabaseFactory =
                new FileDatabaseFactory(
                        new SearchDatabaseDirectoryIO(context));
        return new SearchablePreferenceDAO(fileDatabaseFactory.createFileDatabase(locale));
    }

    private void computeAndPersistPreferences(final SearchablePreferenceDAO searchablePreferenceDAO) {
        // FK-TODO: show progressBar only for computePreferences() and not for load()?
        final Set<SearchablePreference> preferences = computePreferences();
        progressUpdateListener.onProgressUpdate("persisting search database");
        searchablePreferenceDAO.persist(preferences);
    }

    private Set<SearchablePreference> computePreferences() {
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> searchablePreferenceScreenGraph =
                this
                        .getSearchablePreferenceScreenGraphProvider()
                        .getSearchablePreferenceScreenGraph();
        progressUpdateListener.onProgressUpdate("preparing search database");
        return MergedPreferenceScreenDataFactory.getPreferences(searchablePreferenceScreenGraph);
    }

    private SearchablePreferenceScreenGraphProvider getSearchablePreferenceScreenGraphProvider() {
        return new SearchablePreferenceScreenGraphProvider(
                searchDatabaseConfig.rootPreferenceFragment,
                new PreferenceScreenWithHostProvider(
                        instantiateAndInitializeFragment,
                        new SearchablePreferenceScreenProvider(
                                new PreferenceVisibleAndSearchablePredicate(
                                        searchDatabaseConfig.preferenceSearchablePredicate)),
                        principalAndProxyProvider),
                searchDatabaseConfig.preferenceFragmentConnected2PreferenceProvider,
                searchDatabaseConfig.rootPreferenceFragmentOfActivityProvider,
                searchDatabaseConfig.preferenceScreenGraphAvailableListener,
                new PreferenceScreenGraphListener() {

                    @Override
                    public void preferenceScreenWithHostAdded(final PreferenceScreenWithHost preferenceScreenWithHost) {
                        progressUpdateListener.onProgressUpdate(ProgressProvider.getProgress(preferenceScreenWithHost));
                    }
                },
                searchDatabaseConfig.computePreferencesListener,
                new Preference2SearchablePreferenceConverter(
                        new IconProvider(searchDatabaseConfig.iconResourceIdProvider),
                        new SearchableInfoAndDialogInfoProvider(
                                searchDatabaseConfig.searchableInfoProvider,
                                new SearchableDialogInfoOfProvider(
                                        preferenceDialogs,
                                        searchDatabaseConfig.preferenceDialogAndSearchableInfoProvider)),
                        new IdGenerator()),
                context);
    }
}
