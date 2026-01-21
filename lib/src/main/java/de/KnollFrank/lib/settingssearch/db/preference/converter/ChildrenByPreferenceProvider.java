package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class ChildrenByPreferenceProvider {

    private ChildrenByPreferenceProvider() {
    }

    public static Map<SearchablePreference, Set<SearchablePreference>> getChildrenByPreference(
            final Set<SearchablePreference> searchablePreferences) {
        return searchablePreferences
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                SearchablePreference::getChildren));
    }
}
