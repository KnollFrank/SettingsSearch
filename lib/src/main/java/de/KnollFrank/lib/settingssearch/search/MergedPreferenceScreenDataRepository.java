package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import java.io.File;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

import de.KnollFrank.lib.settingssearch.Fragment2PreferenceFragmentConverter;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.common.LockingSupport;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.dao.MergedPreferenceScreenDataFileDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenDataFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.IFragments;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreenGraphListener;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.provider.SearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressProvider;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class MergedPreferenceScreenDataRepository {

    private final IFragments fragments;
    private final DefaultFragmentInitializer preferenceDialogs;
    private final SearchDatabaseConfig searchDatabaseConfig;
    private final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment;
    private final ProgressUpdateListener progressUpdateListener;
    private final SearchDatabaseDirectoryIO searchDatabaseDirectoryIO;
    private final Fragment2PreferenceFragmentConverter fragment2PreferenceFragmentConverter;
    private final Context context;

    public MergedPreferenceScreenDataRepository(
            final IFragments fragments,
            final DefaultFragmentInitializer preferenceDialogs,
            final SearchDatabaseConfig searchDatabaseConfig,
            final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment,
            final ProgressUpdateListener progressUpdateListener,
            final SearchDatabaseDirectoryIO searchDatabaseDirectoryIO,
            final Fragment2PreferenceFragmentConverter fragment2PreferenceFragmentConverter,
            final Context context) {
        this.fragments = fragments;
        this.preferenceDialogs = preferenceDialogs;
        this.searchDatabaseConfig = searchDatabaseConfig;
        this.rootPreferenceFragment = rootPreferenceFragment;
        this.progressUpdateListener = progressUpdateListener;
        this.searchDatabaseDirectoryIO = searchDatabaseDirectoryIO;
        this.fragment2PreferenceFragmentConverter = fragment2PreferenceFragmentConverter;
        this.context = context;
    }

    public Set<SearchablePreference> persistOrLoadPreferences(final Locale locale) {
        synchronized (LockingSupport.searchDatabaseLock) {
            final File directory = searchDatabaseDirectoryIO.getAndMakeSearchDatabaseDirectory4Locale(locale);
            final MergedPreferenceScreenDataFiles dataFiles = getMergedPreferenceScreenDataFiles(directory);
            // FK-TODO: show progressBar only for computeAndPersistMergedPreferenceScreenData() and not for load()?
            if (!exists(dataFiles)) {
                final Set<SearchablePreference> preferences = computePreferences();
                progressUpdateListener.onProgressUpdate("persisting search database");
                MergedPreferenceScreenDataFileDAO.persist(preferences, dataFiles);
            }
            return MergedPreferenceScreenDataFileDAO.load(dataFiles);
        }
    }

    private Set<SearchablePreference> computePreferences() {
        final Graph<PreferenceScreenWithHostClass, SearchablePreferenceEdge> searchablePreferenceScreenGraph =
                this
                        .getSearchablePreferenceScreenGraphProvider()
                        .getSearchablePreferenceScreenGraph();
        progressUpdateListener.onProgressUpdate("preparing search database");
        return MergedPreferenceScreenDataFactory.getPreferences(searchablePreferenceScreenGraph);
    }

    private static MergedPreferenceScreenDataFiles getMergedPreferenceScreenDataFiles(final File directory) {
        return new MergedPreferenceScreenDataFiles(
                new File(directory, "preferences.json"),
                new File(directory, "preference_path_by_preference.json"),
                new File(directory, "host_by_preference.json"));
    }

    private static boolean exists(final MergedPreferenceScreenDataFiles dataFiles) {
        return Stream
                .of(
                        dataFiles.preferences(),
                        dataFiles.preferencePathByPreference(),
                        dataFiles.hostByPreference())
                .allMatch(File::exists);
    }

    private SearchablePreferenceScreenGraphProvider getSearchablePreferenceScreenGraphProvider() {
        return new SearchablePreferenceScreenGraphProvider(
                rootPreferenceFragment,
                new PreferenceScreenWithHostProvider(
                        fragments,
                        new SearchablePreferenceScreenProvider(
                                new PreferenceVisibleAndSearchablePredicate(
                                        searchDatabaseConfig.preferenceSearchablePredicate())),
                        fragment2PreferenceFragmentConverter),
                searchDatabaseConfig.preferenceFragmentConnected2PreferenceProvider(),
                searchDatabaseConfig.rootPreferenceFragmentOfActivityProvider(),
                searchDatabaseConfig.preferenceScreenGraphAvailableListener(),
                new PreferenceScreenGraphListener() {

                    @Override
                    public void preferenceScreenWithHostAdded(final PreferenceScreenWithHost preferenceScreenWithHost) {
                        progressUpdateListener.onProgressUpdate(ProgressProvider.getProgress(preferenceScreenWithHost));
                    }
                },
                new Preference2SearchablePreferenceConverter(
                        new IconProvider(searchDatabaseConfig.iconResourceIdProvider()),
                        new SearchableInfoAndDialogInfoProvider(
                                searchDatabaseConfig.searchableInfoProvider(),
                                new SearchableDialogInfoOfProvider(
                                        preferenceDialogs,
                                        searchDatabaseConfig.preferenceDialogAndSearchableInfoProvider())),
                        new IdGenerator()),
                context);
    }
}
