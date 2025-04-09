package de.KnollFrank.lib.settingssearch;

import static de.KnollFrank.lib.settingssearch.search.PreferenceMatcher.getPreferenceMatch;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Optionals;
import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;

public class SearchablePreferenceDAO {

    private final Set<SearchablePreference> preferences;

    public SearchablePreferenceDAO(final Set<SearchablePreference> preferences) {
        this.preferences = preferences;
    }

    public Set<PreferenceMatch> searchFor(final String needle,
                                          final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate) {
        return SearchablePreferences
                .getPreferencesRecursively(preferences)
                .stream()
                .filter(includePreferenceInSearchResultsPredicate::includePreferenceInSearchResults)
                .map(searchablePreference -> getPreferenceMatch(searchablePreference, needle))
                .flatMap(Optionals::streamOfPresentElements)
                .collect(Collectors.toSet());

    }

    public SearchablePreference getPreferenceFromId(final int id) {
        return SearchablePreferences
                .getPreferencesRecursively(preferences)
                .stream()
                .filter(preference -> preference.getId() == id)
                .findFirst()
                .orElseThrow();

    }
}
