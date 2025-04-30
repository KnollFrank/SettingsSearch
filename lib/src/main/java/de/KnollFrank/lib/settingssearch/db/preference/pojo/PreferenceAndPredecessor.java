package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Optional;

// FK-TODO: make package private
public class PreferenceAndPredecessor {

    @Embedded
    private final SearchablePreference preference;

    @Relation(
            parentColumn = "predecessorId",
            entityColumn = "id")
    private final SearchablePreference predecessor;

    public PreferenceAndPredecessor(final SearchablePreference preference,
                                    final SearchablePreference predecessor) {
        this.preference = preference;
        this.predecessor = predecessor;
    }

    public SearchablePreference getPreference() {
        return preference;
    }

    public Optional<SearchablePreference> getPredecessor() {
        return Optional.ofNullable(predecessor);
    }
}