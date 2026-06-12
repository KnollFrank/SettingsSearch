package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class ImmediateChildrenByPreferenceProvider {

    private ImmediateChildrenByPreferenceProvider() {
    }

    public static Map<SearchablePreference, Set<SearchablePreference>> getImmediateChildrenByPreference(
            final Set<SearchablePreference> searchablePreferences) {
        return searchablePreferences
                .stream()
                .collect(
                        Collectors.toUnmodifiableMap(
                                Function.identity(),
                                SearchablePreference::getImmediateChildren));
    }
}
