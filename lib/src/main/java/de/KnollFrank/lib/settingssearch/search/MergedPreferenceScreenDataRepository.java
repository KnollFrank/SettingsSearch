package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import java.io.File;
import java.util.Locale;
import java.util.stream.Stream;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenProvider;
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
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.SearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.search.progress.IProgressDisplayer;
import de.KnollFrank.lib.settingssearch.search.provider.IconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

class MergedPreferenceScreenDataRepository {

    private final Fragments fragments;
    private final DefaultFragmentInitializer preferenceDialogs;
    private final PreferenceScreenGraphListener preferenceScreenGraphListener;
    private final IconResourceIdProvider iconResourceIdProvider;
    private final SearchableInfoProvider searchableInfoProvider;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    private final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment;
    private final PreferenceSearchablePredicate preferenceSearchablePredicate;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final Locale locale;
    private final Context context;
    private final IProgressDisplayer progressDisplayer;

    public MergedPreferenceScreenDataRepository(
            final Fragments fragments,
            final DefaultFragmentInitializer preferenceDialogs,
            final PreferenceScreenGraphListener preferenceScreenGraphListener,
            final IconResourceIdProvider iconResourceIdProvider,
            final SearchableInfoProvider searchableInfoProvider,
            final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
            final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment,
            final PreferenceSearchablePredicate preferenceSearchablePredicate,
            final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
            final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
            final Locale locale,
            final Context context,
            final IProgressDisplayer progressDisplayer) {
        this.fragments = fragments;
        this.preferenceDialogs = preferenceDialogs;
        this.preferenceScreenGraphListener = preferenceScreenGraphListener;
        this.iconResourceIdProvider = iconResourceIdProvider;
        this.searchableInfoProvider = searchableInfoProvider;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.rootPreferenceFragment = rootPreferenceFragment;
        this.preferenceSearchablePredicate = preferenceSearchablePredicate;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.locale = locale;
        this.context = context;
        this.progressDisplayer = progressDisplayer;
    }

    public MergedPreferenceScreenData getMergedPreferenceScreenData() {
        final File directory = new SearchDatabaseDirectoryIO(context).getAndMakeSearchDatabaseDirectory4Locale(locale);
        final MergedPreferenceScreenDataFiles dataFiles = getMergedPreferenceScreenDataFiles(directory);
        // FK-TODO: show progressBar only for computeAndPersistMergedPreferenceScreenData() and not for load()?
        if (!exists(dataFiles)) {
            final MergedPreferenceScreenData mergedPreferenceScreenData = computeMergedPreferenceScreenData();
            progressDisplayer.displayProgress("persisting search database");
            MergedPreferenceScreenDataFileDAO.persist(mergedPreferenceScreenData, dataFiles);
        }
        return MergedPreferenceScreenDataFileDAO.load(dataFiles);
    }

    private MergedPreferenceScreenData computeMergedPreferenceScreenData() {
        final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> searchablePreferenceScreenGraph =
                this
                        .getSearchablePreferenceScreenGraphProvider()
                        .getSearchablePreferenceScreenGraph();
        progressDisplayer.displayProgress("preparing search database");
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
                                        preferenceSearchablePredicate))),
                preferenceConnected2PreferenceFragmentProvider,
                preferenceScreenGraphAvailableListener,
                preferenceScreenGraphListener,
                new Preference2SearchablePreferencePOJOConverter(
                        new IconProvider(iconResourceIdProvider),
                        new SearchableInfoAndDialogInfoProvider(
                                searchableInfoProvider,
                                new SearchableDialogInfoOfProvider(
                                        preferenceDialogs,
                                        preferenceDialogAndSearchableInfoProvider)),
                        new IdGenerator()));
    }
}
