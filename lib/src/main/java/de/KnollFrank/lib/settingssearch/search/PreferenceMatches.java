package de.KnollFrank.lib.settingssearch.search;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;

public class PreferenceMatches {

    private PreferenceMatches() {
    }

    public static Set<SearchablePreferenceOfHostWithinTree> getPreferences(final Set<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(PreferenceMatch::preference)
                .collect(Collectors.toSet());
    }
}
