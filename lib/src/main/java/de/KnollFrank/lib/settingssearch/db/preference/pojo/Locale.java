package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// FK-TODO: remove
@Entity
public record Locale(@PrimaryKey @NonNull String language) {
}
