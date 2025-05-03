package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public record Locale(@PrimaryKey @NonNull String languageCode) {
}
