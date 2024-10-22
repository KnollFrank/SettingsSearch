package de.KnollFrank.lib.settingssearch.common;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class SearchablePreferences {

    public static List<SearchablePreferencePOJO> getPreferencesRecursively(final SearchablePreferencePOJO preference) {
        return ImmutableList
                .<SearchablePreferencePOJO>builder()
                .add(preference)
                .addAll(getPreferencesRecursively(preference.children()))
                .build();
    }

    public static List<SearchablePreferencePOJO> getPreferencesRecursively(final Collection<SearchablePreferencePOJO> preferences) {
        return Lists.concat(
                preferences
                        .stream()
                        .map(SearchablePreferences::getPreferencesRecursively)
                        .collect(Collectors.toList()));
    }
}
