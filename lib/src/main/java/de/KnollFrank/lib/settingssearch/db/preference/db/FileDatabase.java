package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.dao.MergedPreferenceScreenDataFileDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.search.MergedPreferenceScreenDataFiles;

public class FileDatabase implements Database {

    private final MergedPreferenceScreenDataFiles dataFiles;

    public FileDatabase(final MergedPreferenceScreenDataFiles dataFiles) {
        this.dataFiles = dataFiles;
    }

    @Override
    public void persist(final Set<SearchablePreference> preferences) {
        MergedPreferenceScreenDataFileDAO.persist(preferences, dataFiles);
    }

    @Override
    public Set<SearchablePreference> load() {
        // FK-TODO: cache loaded preferences
        return MergedPreferenceScreenDataFileDAO.load(dataFiles);
    }

    @Override
    public boolean isInitialized() {
        return dataFiles.exists();
    }
}
