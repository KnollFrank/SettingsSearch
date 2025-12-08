package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Set;

// FK-TODO: hat N+1 Problem, verwende JOIN
public record PreferenceAndChildren(
        @Embedded
        SearchablePreferenceEntity preference,
        @Relation(
                parentColumn = "id",
                entityColumn = "parentId")
        Set<SearchablePreferenceEntity> children) {
}
