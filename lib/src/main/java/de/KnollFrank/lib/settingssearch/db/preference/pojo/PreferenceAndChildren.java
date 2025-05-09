package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Set;

public record PreferenceAndChildren(
        @Embedded
        SearchablePreference preference,
        @Relation(
                parentColumn = "id",
                entityColumn = "parentId")
        Set<SearchablePreference> children) {
}