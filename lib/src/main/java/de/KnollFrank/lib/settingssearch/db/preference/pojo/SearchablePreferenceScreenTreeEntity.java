package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;
import java.util.Set;

@Entity
public record SearchablePreferenceScreenTreeEntity(@PrimaryKey @NonNull Locale id,
                                                   PersistableBundle configuration) {

    public interface DbDataProvider {

        Set<SearchablePreferenceScreenEntity> getNodes(SearchablePreferenceScreenTreeEntity tree);
    }

    public Set<SearchablePreferenceScreenEntity> getNodes(final DbDataProvider dbDataProvider) {
        return dbDataProvider.getNodes(this);
    }
}
