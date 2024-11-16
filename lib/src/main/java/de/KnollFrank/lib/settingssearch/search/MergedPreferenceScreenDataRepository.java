package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import java.io.File;
import java.util.Locale;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.db.preference.dao.MergedPreferenceScreenDataFileDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.search.progress.IProgressDisplayer;

class MergedPreferenceScreenDataRepository {

    public static MergedPreferenceScreenData getMergedPreferenceScreenData(
            final Supplier<MergedPreferenceScreenData> computeMergedPreferenceScreenData,
            final Locale locale,
            final Context context,
            final IProgressDisplayer progressDisplayer) {
        final File directory = getDirectory4Locale(locale, context);
        final MergedPreferenceScreenDataFiles dataFiles = getMergedPreferenceScreenDataFiles(directory);
        // FK-TODO: show progressBar only for computeAndPersistMergedPreferenceScreenData() and not for load()?
        // FK-TODO: eine Löschung der dataFiles ermöglichen, damit eine Neuberechnung stattfindet (z.B. durch neue Plugins in OsmAnd ausgelöst).
        if (exists(dataFiles)) {
            progressDisplayer.displayProgress("loading search database");
            return MergedPreferenceScreenDataFileDAO.load(dataFiles);
        } else {
            progressDisplayer.displayProgress("preparing search database");
            final MergedPreferenceScreenData mergedPreferenceScreenData = computeMergedPreferenceScreenData.get();
            progressDisplayer.displayProgress("persisting search database");
            MergedPreferenceScreenDataFileDAO.persist(mergedPreferenceScreenData, dataFiles);
            return mergedPreferenceScreenData;
        }
    }

    private static File getDirectory4Locale(final Locale locale, final Context context) {
        final File directory =
                new File(
                        context.getDir("settingssearch", Context.MODE_PRIVATE),
                        locale.getLanguage());
        directory.mkdirs();
        return directory;
    }

    private static MergedPreferenceScreenDataFiles getMergedPreferenceScreenDataFiles(final File directory) {
        return new MergedPreferenceScreenDataFiles(
                new File(directory, "preferences.json"),
                new File(directory, "preference_path_by_preference.json"),
                new File(directory, "host_by_preference.json"));
    }

    private static boolean exists(final MergedPreferenceScreenDataFiles mergedPreferenceScreenDataFiles) {
        return mergedPreferenceScreenDataFiles.preferences().exists();
    }
}
