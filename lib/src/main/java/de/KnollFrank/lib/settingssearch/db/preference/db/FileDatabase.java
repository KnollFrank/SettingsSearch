package de.KnollFrank.lib.settingssearch.db.preference.db;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

// FK-TODO: write tests for FileDatabase
class FileDatabase implements Database {

    private final MergedPreferenceScreenDataFiles dataFiles;
    private Optional<Set<SearchablePreference>> cache = Optional.empty();

    public FileDatabase(final MergedPreferenceScreenDataFiles dataFiles) {
        this.dataFiles = dataFiles;
    }

    @Override
    public void initializeWith(final Set<SearchablePreference> preferences) {
        MergedPreferenceScreenDataFileDAO.persist(preferences, dataFiles);
        cache = Optional.empty();
    }

    @Override
    public boolean isInitialized() {
        return dataFiles.exists();
    }

    @Override
    public Set<SearchablePreference> loadAll() {
        if (cache.isEmpty()) {
            cache = Optional.of(MergedPreferenceScreenDataFileDAO.load(dataFiles));
        }
        return cache.orElseThrow();
    }

    @Override
    public void persistPreference(final SearchablePreference preference) {
        initializeWith(
                ImmutableSet
                        .<SearchablePreference>builder()
                        .addAll(loadAll())
                        .add(preference)
                        .build());
    }

    @Override
    public void removePreference(final int idOfPreference) {
        initializeWith(
                Sets.difference(
                        loadAll(),
                        // FK-FIXME: getPreferenceById() könnte auch ein Kind zurückgeben, welches dann nicht in loadAll() auftaucht und deswegen fälschlicherweise nicht aus der DB entfernt wird.
                        Set.of(getPreferenceById(loadAll(), idOfPreference))));
    }

    @Override
    public void updateSummary(final int idOfPreference, final String newSummaryOfPreference) {
        final Set<SearchablePreference> preferences = loadAll();
        getPreferenceById(preferences, idOfPreference).setSummary(newSummaryOfPreference);
        initializeWith(preferences);
    }

    private static SearchablePreference getPreferenceById(final Set<SearchablePreference> preferences,
                                                          final int id) {
        return SearchablePreferences.findUniquePreferenceRecursivelyByPredicate(
                preferences,
                preference -> preference.getId() == id);
    }
}
