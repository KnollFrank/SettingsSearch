package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static de.KnollFrank.lib.settingssearch.common.IOUtils.getFileInputStream;
import static de.KnollFrank.lib.settingssearch.common.IOUtils.getFileOutputStream;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataFiles;

public class MergedPreferenceScreenDataFileDAO {

    public static void persist(final Set<SearchablePreference> preferences,
                               final MergedPreferenceScreenDataFiles sink) {
        MergedPreferenceScreenDataDAO.persist(
                preferences,
                getFileOutputStream(sink.preferences()),
                getFileOutputStream(sink.preferencePathByPreference()),
                getFileOutputStream(sink.hostByPreference()));
    }

    public static Set<SearchablePreference> load(final MergedPreferenceScreenDataFiles source) {
        return MergedPreferenceScreenDataDAO.load(
                getFileInputStream(source.preferences()),
                getFileInputStream(source.preferencePathByPreference()),
                getFileInputStream(source.hostByPreference()));
    }
}
