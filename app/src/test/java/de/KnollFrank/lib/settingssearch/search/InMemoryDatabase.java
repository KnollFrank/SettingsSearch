package de.KnollFrank.lib.settingssearch.search;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.preference.db.Database;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

// FK-TODO: write tests for InMemoryDatabase
class InMemoryDatabase implements Database {

    private Optional<Set<SearchablePreference>> preferences = Optional.empty();

    @Override
    public void initializeWith(final Set<SearchablePreference> preferences) {
        this.preferences = Optional.of(preferences);
    }

    @Override
    public boolean isInitialized() {
        return preferences.isPresent();
    }

    @Override
    public Set<SearchablePreference> loadAll() {
        return preferences.orElseThrow();
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
                        Set.of(getPreferenceById(idOfPreference))));
    }

    @Override
    public void updateSummary(final int idOfPreference, final String newSummaryOfPreference) {
        getPreferenceById(idOfPreference).setSummary(newSummaryOfPreference);
    }

    @Override
    public int getUnusedId() {
        return getMaxId().map(maxId -> maxId + 1).orElse(0);
    }

    private Optional<Integer> getMaxId() {
        return isInitialized() ?
                this
                        .loadAll()
                        .stream()
                        .map(SearchablePreference::getId)
                        .max(Integer::compareTo) :
                Optional.empty();
    }

    private SearchablePreference getPreferenceById(final int id) {
        return SearchablePreferences.findUniquePreferenceRecursivelyByPredicate(
                loadAll(),
                preference -> preference.getId() == id);
    }
}
