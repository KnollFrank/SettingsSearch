package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public record PreferenceAndChildren(
        @Embedded SearchablePreferencePOJO preference,
        @Relation(
                parentColumn = "id",
                entityColumn = "parentId")
        List<SearchablePreferencePOJO> children) {
}