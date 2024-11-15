package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static de.KnollFrank.lib.settingssearch.common.IOUtils.getFileInputStream;
import static de.KnollFrank.lib.settingssearch.common.IOUtils.getFileOutputStream;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataFiles;

public class MergedPreferenceScreenDataFileDAO {

    public static void persist(final MergedPreferenceScreenData mergedPreferenceScreenData,
                               final MergedPreferenceScreenDataFiles sink) {
        MergedPreferenceScreenDataDAO.persist(
                mergedPreferenceScreenData,
                getFileOutputStream(sink.preferences()),
                getFileOutputStream(sink.preferencePathByPreference()),
                getFileOutputStream(sink.hostByPreference()));
    }

    public static MergedPreferenceScreenData load(final MergedPreferenceScreenDataFiles source) {
        return MergedPreferenceScreenDataDAO.load(
                getFileInputStream(source.preferences()),
                getFileInputStream(source.preferencePathByPreference()),
                getFileInputStream(source.hostByPreference()));
    }
}
