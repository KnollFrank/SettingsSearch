package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter {

    public static SearchablePreferenceScreenEntity toEntity(final SearchablePreferenceScreen screen) {
        final Set<SearchablePreference> searchablePreferences = screen.allPreferences();
        final Set<SearchablePreferenceEntity> allPreferences =
                searchablePreferences
                        .stream()
                        .map(
                                searchablePreference ->
                                        SearchablePreferenceToSearchablePreferenceEntityConverter.toEntity(
                                                searchablePreference,
                                                Optional.empty(),
                                                ""))
                        .collect(Collectors.toSet());
        return new SearchablePreferenceScreenEntity(
                screen.id(),
                screen.host(),
                screen.title(),
                screen.summary(),
                allPreferences);
    }
}
