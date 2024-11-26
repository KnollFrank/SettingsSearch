package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import java.io.File;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Stream;

import de.KnollFrank.lib.settingssearch.db.preference.dao.MergedPreferenceScreenDataFileDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.search.progress.IProgressDisplayer;

class MergedPreferenceScreenDataRepository {

    public static MergedPreferenceScreenData getMergedPreferenceScreenData(
            final Supplier<MergedPreferenceScreenData> computeMergedPreferenceScreenData,
            final Locale locale,
            final Context context,
            final IProgressDisplayer progressDisplayer) {
        final MergedPreferenceScreenDataFiles dataFiles =
                computeAndPersistMergedPreferenceScreenDataFiles(
                        computeMergedPreferenceScreenData,
                        locale,
                        context,
                        progressDisplayer);
        return MergedPreferenceScreenDataFileDAO.load(dataFiles);
    }

    private static MergedPreferenceScreenDataFiles computeAndPersistMergedPreferenceScreenDataFiles(
            final Supplier<MergedPreferenceScreenData> computeMergedPreferenceScreenData,
            final Locale locale,
            final Context context,
            final IProgressDisplayer progressDisplayer) {
        final File directory = new SearchDatabaseDirectoryIO(context).getAndMakeSearchDatabaseDirectory4Locale(locale);
        final MergedPreferenceScreenDataFiles dataFiles = getMergedPreferenceScreenDataFiles(directory);
        // FK-TODO: show progressBar only for computeAndPersistMergedPreferenceScreenData() and not for load()?
        if (!exists(dataFiles)) {
            final MergedPreferenceScreenData mergedPreferenceScreenData = computeMergedPreferenceScreenData.get();
            progressDisplayer.displayProgress("persisting search database");
            MergedPreferenceScreenDataFileDAO.persist(mergedPreferenceScreenData, dataFiles);
        }
        return dataFiles;
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
}
