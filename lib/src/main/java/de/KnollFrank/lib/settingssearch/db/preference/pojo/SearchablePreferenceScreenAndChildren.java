package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public record SearchablePreferenceScreenAndChildren(
        @Embedded
        SearchablePreferenceScreen searchablePreferenceScreen,
        @Relation(
                parentColumn = "id",
                entityColumn = "parentId")
        List<SearchablePreferenceScreen> children) {
}