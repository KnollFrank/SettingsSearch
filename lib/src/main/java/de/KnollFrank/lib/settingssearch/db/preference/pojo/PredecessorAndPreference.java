package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

// FK-TODO: refactor: make predecessor an Optional<SearchablePreferencePOJO>
public class PredecessorAndPreference {

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

    public SearchablePreferencePOJO getPredecessor() {
        return predecessor;
    }
}