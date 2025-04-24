package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Optional;

class PredecessorAndPreference {

    @Embedded
    private final SearchablePreferencePOJO preference;

    @Relation(
            parentColumn = "predecessorId",
            entityColumn = "id")
    private final SearchablePreferencePOJO predecessor;

    public PredecessorAndPreference(final SearchablePreferencePOJO preference,
                                    final SearchablePreferencePOJO predecessor) {
        this.preference = preference;
        this.predecessor = predecessor;
    }

    public SearchablePreferencePOJO getPreference() {
        return preference;
    }

    public Optional<SearchablePreferencePOJO> getPredecessor() {
        return Optional.ofNullable(predecessor);
    }
}