package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Optional;

// FK-TODO: hat N+1 Problem, verwende JOIN
public class PreferenceAndPredecessor {

    @Embedded
    private final SearchablePreferenceEntity preference;

    @Relation(
            parentColumn = "predecessorId",
            entityColumn = "id")
    private final SearchablePreferenceEntity predecessor;

    public PreferenceAndPredecessor(final SearchablePreferenceEntity preference,
                                    final SearchablePreferenceEntity predecessor) {
        this.preference = preference;
        this.predecessor = predecessor;
    }

    public SearchablePreferenceEntity getPreference() {
        return preference;
    }

    public Optional<SearchablePreferenceEntity> getPredecessor() {
        return Optional.ofNullable(predecessor);
    }
}