package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class FileDatabase implements Database {

    private final MergedPreferenceScreenDataFiles dataFiles;
    private Optional<Set<SearchablePreference>> cache = Optional.empty();

    public FileDatabase(final MergedPreferenceScreenDataFiles dataFiles) {
        this.dataFiles = dataFiles;
    }

    @Override
    public void persist(final Set<SearchablePreference> preferences) {
        MergedPreferenceScreenDataFileDAO.persist(preferences, dataFiles);
        cache = Optional.of(preferences);
    }

    @Override
    public void updateSummary(final int idOfPreference, final String newSummaryOfPreference) {
        final Set<SearchablePreference> preferences = loadAll();
        getPreferenceById(preferences, idOfPreference).setSummary(newSummaryOfPreference);
        persist(preferences);
    }

    @Override
    public Set<SearchablePreference> loadAll() {
        if (cache.isEmpty()) {
            cache = Optional.of(MergedPreferenceScreenDataFileDAO.load(dataFiles));
        }
        return cache.orElseThrow();
    }

    @Override
    public boolean isInitialized() {
        return dataFiles.exists();
    }

    private static SearchablePreference getPreferenceById(final Set<SearchablePreference> preferences,
                                                          final int id) {
        return SearchablePreferences.findPreferenceRecursivelyByPredicate(preferences, preference -> preference.getId() == id);
    }
}
