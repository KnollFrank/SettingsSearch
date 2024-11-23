package de.KnollFrank.lib.settingssearch.search;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class PreferenceMatchHelper {

    public static Set<String> getKeySet(final Set<PreferenceMatch> preferenceMatches) {
        return PreferenceMatchHelper
                .getKeyStream(preferenceMatches)
                .collect(Collectors.toSet());
    }

    public static List<String> getKeyList(final Set<PreferenceMatch> preferenceMatches) {
        return PreferenceMatchHelper
                .getKeyStream(preferenceMatches)
                .collect(Collectors.toList());
    }

    private static Stream<String> getKeyStream(final Set<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(PreferenceMatch::preference)
                .map(SearchablePreferencePOJO::getKey)
                .flatMap(Optional::stream);
    }
}
