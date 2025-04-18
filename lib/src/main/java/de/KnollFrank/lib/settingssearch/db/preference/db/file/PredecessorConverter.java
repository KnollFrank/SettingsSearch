package de.KnollFrank.lib.settingssearch.db.preference.db.file;

import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class PredecessorConverter {

    public static Optional<Integer> addIds(final Optional<SearchablePreference> predecessor) {
        return predecessor.map(SearchablePreference::getId);
    }

    public static Optional<SearchablePreference> removeIds(final Optional<Integer> predecessorId,
                                                           final Map<Integer, SearchablePreference> preferenceById) {
        return predecessorId.map(preferenceById::get);
    }
}
