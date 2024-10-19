package de.KnollFrank.lib.settingssearch.common;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class SearchablePreferences {

    public static List<SearchablePreferencePOJO> getAllPreferences(final Collection<SearchablePreferencePOJO> preferences) {
        return Lists.concat(
                preferences
                        .stream()
                        .map(SearchablePreferences::getAllPreferences)
                        .collect(Collectors.toList()));
    }

    private static List<SearchablePreferencePOJO> getAllPreferences(final SearchablePreferencePOJO preference) {
        return ImmutableList
                .<SearchablePreferencePOJO>builder()
                .add(preference)
                .addAll(getAllPreferences(preference.children()))
                .build();
    }
}
