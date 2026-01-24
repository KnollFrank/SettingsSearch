package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.PersistableBundle;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.codepoetics.ambivalence.Either;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;

@Entity
public record TreeProcessorDescriptionEntity<C>(
        @PrimaryKey(autoGenerate = true)
        long id,
        Either<Class<? extends SearchablePreferenceScreenTreeCreator<C>>, Class<? extends SearchablePreferenceScreenTreeTransformer<C>>> treeProcessor,
        PersistableBundle params) {
}

