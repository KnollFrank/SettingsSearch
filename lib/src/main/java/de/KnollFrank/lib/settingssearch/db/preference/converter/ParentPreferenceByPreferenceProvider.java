package de.KnollFrank.lib.settingssearch.db.preference.converter;

import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

class ParentPreferenceByPreferenceProvider {

    public static Map<SearchablePreference, Optional<SearchablePreference>> getParentPreferenceByPreference(final SearchablePreferenceScreen screen) {
        return getParentPreferenceByPreference(screen.allPreferences());
    }

    private static Map<SearchablePreference, Optional<SearchablePreference>> getParentPreferenceByPreference(final Set<SearchablePreference> searchablePreferences) {
        final Map<SearchablePreference, SearchablePreference> parentPreferenceByPreference =
                _getParentPreferenceByPreference(searchablePreferences);
        return Maps.merge(
                List.of(
                        Maps.mapValues(
                                parentPreferenceByPreference,
                                Optional::of),
                        Maps.mapEachKeyToValue(
                                getPreferencesWithoutParent(
                                        searchablePreferences,
                                        parentPreferenceByPreference.keySet()),
                                Optional.empty())));
    }

    private static Set<SearchablePreference> getPreferencesWithoutParent(final Set<SearchablePreference> allPreferences,
                                                                         final Set<SearchablePreference> preferencesWithParent) {
        return Sets.difference(allPreferences, preferencesWithParent);
    }

    private static Map<SearchablePreference, SearchablePreference> _getParentPreferenceByPreference(final Set<SearchablePreference> searchablePreferences) {
        return Maps.merge(
                searchablePreferences
                        .stream()
                        .map(ParentPreferenceByPreferenceProvider::getParentPreferenceByPreference)
                        .collect(Collectors.toSet()));
    }

    private static Map<SearchablePreference, SearchablePreference> getParentPreferenceByPreference(final SearchablePreference searchablePreference) {
        return Maps.mapEachKeyToValue(
                searchablePreference.getChildren(),
                searchablePreference);
    }
}
