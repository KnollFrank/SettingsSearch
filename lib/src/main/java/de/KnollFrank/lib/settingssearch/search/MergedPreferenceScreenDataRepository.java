package de.KnollFrank.lib.settingssearch.search;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import java.io.File;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.client.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferencePOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.dao.MergedPreferenceScreenDataFileDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenDataFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreenGraphListener;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.provider.SearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressProvider;
import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class MergedPreferenceScreenDataRepository {

    private final Fragments fragments;
    private final DefaultFragmentInitializer preferenceDialogs;
    private final SearchDatabaseConfig searchDatabaseConfig;
    private final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment;
    private final ProgressUpdateListener progressUpdateListener;
    private final SearchDatabaseDirectoryIO searchDatabaseDirectoryIO;

    public MergedPreferenceScreenDataRepository(
            final Fragments fragments,
            final DefaultFragmentInitializer preferenceDialogs,
            final SearchDatabaseConfig searchDatabaseConfig,
            final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment,
            final ProgressUpdateListener progressUpdateListener,
            final SearchDatabaseDirectoryIO searchDatabaseDirectoryIO) {
        this.fragments = fragments;
        this.preferenceDialogs = preferenceDialogs;
        this.searchDatabaseConfig = searchDatabaseConfig;
        this.rootPreferenceFragment = rootPreferenceFragment;
        this.progressUpdateListener = progressUpdateListener;
        this.searchDatabaseDirectoryIO = searchDatabaseDirectoryIO;
    }

    public Set<SearchablePreferencePOJO> persistOrLoadPreferences(final Locale locale) {
        final File directory = searchDatabaseDirectoryIO.getAndMakeSearchDatabaseDirectory4Locale(locale);
        final MergedPreferenceScreenDataFiles dataFiles = getMergedPreferenceScreenDataFiles(directory);
        // FK-TODO: show progressBar only for computeAndPersistMergedPreferenceScreenData() and not for load()?
        if (!exists(dataFiles)) {
            final Set<SearchablePreferencePOJO> preferences = computePreferences();
            progressUpdateListener.onProgressUpdate("persisting search database");
            MergedPreferenceScreenDataFileDAO.persist(preferences, dataFiles);
        }
        return MergedPreferenceScreenDataFileDAO.load(dataFiles);
    }

    private Set<SearchablePreferencePOJO> computePreferences() {
        final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> searchablePreferenceScreenGraph =
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
                rootPreferenceFragment.getName(),
                new PreferenceScreenWithHostProvider(
                        fragments,
                        new SearchablePreferenceScreenProvider(
                                new PreferenceVisibleAndSearchablePredicate(
                                        searchDatabaseConfig.preferenceSearchablePredicate()))),
                searchDatabaseConfig.preferenceFragmentConnected2PreferenceProvider(),
                searchDatabaseConfig.preferenceScreenGraphAvailableListener(),
                new PreferenceScreenGraphListener() {

                    @Override
                    public void preferenceScreenWithHostAdded(final PreferenceScreenWithHost preferenceScreenWithHost) {
                        progressUpdateListener.onProgressUpdate(ProgressProvider.getProgress(preferenceScreenWithHost));
                    }
                },
                new Preference2SearchablePreferencePOJOConverter(
                        new IconProvider(searchDatabaseConfig.iconResourceIdProvider()),
                        new SearchableInfoAndDialogInfoProvider(
                                searchDatabaseConfig.searchableInfoProvider(),
                                new SearchableDialogInfoOfProvider(
                                        preferenceDialogs,
                                        searchDatabaseConfig.preferenceDialogAndSearchableInfoProvider())),
                        new IdGenerator()));
    }
}
