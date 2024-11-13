package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.RawRes;

import org.jgrapht.Graph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.db.preference.dao.MergedPreferenceScreenDataDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenDataFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

class ComputeAndPersistMergedPreferenceScreenData {

    public static MergedPreferenceScreenData computeAndPersistMergedPreferenceScreenData(
            final Supplier<Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge>> searchablePreferenceScreenGraphSupplier,
            final MergedPreferenceScreenDataInput mergedPreferenceScreenDataInput,
            final Context context,
            final Resources resources) {
        final MergedPreferenceScreenData mergedPreferenceScreenData =
                MergedPreferenceScreenDataFactory.getMergedPreferenceScreenData(
                        searchablePreferenceScreenGraphSupplier.get());
        MergedPreferenceScreenDataDAO.persist(
                mergedPreferenceScreenData,
                getFileOutputStream(getFileName(mergedPreferenceScreenDataInput.preferences(), resources), context),
                getFileOutputStream(getFileName(mergedPreferenceScreenDataInput.preferencePathByPreference(), resources), context),
                getFileOutputStream(getFileName(mergedPreferenceScreenDataInput.hostByPreference(), resources), context));
        return mergedPreferenceScreenData;
    }

    public static String getFileName(final @RawRes int id, final Resources resources) {
        return resources.getResourceEntryName(id) + ".json";
    }

    private static FileOutputStream getFileOutputStream(final String fileName, final Context context) {
        try {
            return context.openFileOutput(fileName, Context.MODE_PRIVATE);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileInputStream getFileInputStream(final String fileName, final Context context) {
        try {
            return context.openFileInput(fileName);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
