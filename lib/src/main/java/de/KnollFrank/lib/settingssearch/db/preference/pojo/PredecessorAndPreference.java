package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

// FK-TODO: refactor: use constructor, make fields private and make predecessor an Optional<SearchablePreferencePOJO>
public class PredecessorAndPreference {

    @Embedded
    public SearchablePreferencePOJO preference;

    @Relation(
            parentColumn = "predecessorId",
            entityColumn = "id")
    public SearchablePreferencePOJO predecessor;
}