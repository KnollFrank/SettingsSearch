package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.LanguageCode;

@Entity
public record SearchablePreferenceScreenTreeEntity(@PrimaryKey @NonNull LanguageCode id,
                                                   PersistableBundle configuration) {

    public interface DbDataProvider {

        Set<SearchablePreferenceScreenEntity> getNodes(SearchablePreferenceScreenTreeEntity tree);
    }

    public Set<SearchablePreferenceScreenEntity> getNodes(final DbDataProvider dbDataProvider) {
        return dbDataProvider.getNodes(this);
    }
}
