package de.KnollFrank.lib.settingssearch.db.preference.db.file;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class PredecessorByPreferenceConverter {

    public static Map<Integer, Optional<Integer>> addIds(final Set<SearchablePreference> preferences) {
        return preferences
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreference::getId,
                                preference -> PredecessorConverter.addIds(preference.getPredecessor())));
    }

    public static Map<SearchablePreference, Optional<SearchablePreference>> removeIds(
            final Map<Integer, Optional<Integer>> predecessorIdByPreferenceId,
            final Map<Integer, SearchablePreference> preferenceById) {
        return predecessorIdByPreferenceId
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                preferenceId_predecessorId -> preferenceById.get(preferenceId_predecessorId.getKey()),
                                preferenceId_predecessorId -> PredecessorConverter.removeIds(preferenceId_predecessorId.getValue(), preferenceById)));
    }
}
