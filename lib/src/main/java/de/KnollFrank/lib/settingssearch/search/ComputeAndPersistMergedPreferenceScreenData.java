package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.common.IOUtils.getFileOutputStream;

import org.jgrapht.Graph;

import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.db.preference.dao.MergedPreferenceScreenDataDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenDataFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

class ComputeAndPersistMergedPreferenceScreenData {

    public static MergedPreferenceScreenData computeAndPersistMergedPreferenceScreenData(
            final Supplier<Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge>> searchablePreferenceScreenGraphSupplier,
            final MergedPreferenceScreenDataFiles mergedPreferenceScreenDataFiles) {
        final MergedPreferenceScreenData mergedPreferenceScreenData =
                MergedPreferenceScreenDataFactory.getMergedPreferenceScreenData(
                        searchablePreferenceScreenGraphSupplier.get());
        MergedPreferenceScreenDataDAO.persist(
                mergedPreferenceScreenData,
                getFileOutputStream(mergedPreferenceScreenDataFiles.preferences()),
                getFileOutputStream(mergedPreferenceScreenDataFiles.preferencePathByPreference()),
                getFileOutputStream(mergedPreferenceScreenDataFiles.hostByPreference()));
        return mergedPreferenceScreenData;
    }
}
