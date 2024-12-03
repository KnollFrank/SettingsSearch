package de.KnollFrank.lib.settingssearch.search;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import java.io.File;
import java.util.Locale;
import java.util.stream.Stream;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.client.SearchDatabase;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferencePOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.dao.MergedPreferenceScreenDataFileDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenDataFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
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
    private final SearchDatabase searchDatabase;
    private final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment;
    private final ProgressUpdateListener progressUpdateListener;
    private final SearchDatabaseDirectoryIO searchDatabaseDirectoryIO;

    public MergedPreferenceScreenDataRepository(
            final Fragments fragments,
            final DefaultFragmentInitializer preferenceDialogs,
            final SearchDatabase searchDatabase,
            final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment,
            final ProgressUpdateListener progressUpdateListener,
            final SearchDatabaseDirectoryIO searchDatabaseDirectoryIO) {
        this.fragments = fragments;
        this.preferenceDialogs = preferenceDialogs;
        this.searchDatabase = searchDatabase;
        this.rootPreferenceFragment = rootPreferenceFragment;
        this.progressUpdateListener = progressUpdateListener;
        this.searchDatabaseDirectoryIO = searchDatabaseDirectoryIO;
    }

    public MergedPreferenceScreenData persistOrLoadMergedPreferenceScreenData(final Locale locale) {
        final File directory = searchDatabaseDirectoryIO.getAndMakeSearchDatabaseDirectory4Locale(locale);
        final MergedPreferenceScreenDataFiles dataFiles = getMergedPreferenceScreenDataFiles(directory);
        // FK-TODO: show progressBar only for computeAndPersistMergedPreferenceScreenData() and not for load()?
        if (!exists(dataFiles)) {
            final MergedPreferenceScreenData mergedPreferenceScreenData = computeMergedPreferenceScreenData();
            progressUpdateListener.onProgressUpdate("persisting search database");
            MergedPreferenceScreenDataFileDAO.persist(mergedPreferenceScreenData, dataFiles);
        }
        return MergedPreferenceScreenDataFileDAO.load(dataFiles);
    }

    private MergedPreferenceScreenData computeMergedPreferenceScreenData() {
        final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> searchablePreferenceScreenGraph =
                this
                        .getSearchablePreferenceScreenGraphProvider()
                        .getSearchablePreferenceScreenGraph();
        progressUpdateListener.onProgressUpdate("preparing search database");
        return MergedPreferenceScreenDataFactory.getMergedPreferenceScreenData(searchablePreferenceScreenGraph);
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
                                        searchDatabase.preferenceSearchablePredicate()))),
                searchDatabase.preferenceFragmentConnected2PreferenceProvider(),
                searchDatabase.preferenceScreenGraphAvailableListener(),
                new PreferenceScreenGraphListener() {

                    @Override
                    public void preferenceScreenWithHostAdded(final PreferenceScreenWithHost preferenceScreenWithHost) {
                        progressUpdateListener.onProgressUpdate(ProgressProvider.getProgress(preferenceScreenWithHost));
                    }
                },
                new Preference2SearchablePreferencePOJOConverter(
                        new IconProvider(searchDatabase.iconResourceIdProvider()),
                        new SearchableInfoAndDialogInfoProvider(
                                searchDatabase.searchableInfoProvider(),
                                new SearchableDialogInfoOfProvider(
                                        preferenceDialogs,
                                        searchDatabase.preferenceDialogAndSearchableInfoProvider())),
                        new IdGenerator()));
    }
}
