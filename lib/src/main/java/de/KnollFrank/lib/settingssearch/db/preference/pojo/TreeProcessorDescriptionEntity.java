package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.PersistableBundle;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.codepoetics.ambivalence.Either;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;

@Entity
public record TreeProcessorDescriptionEntity(
        @PrimaryKey(autoGenerate = true)
        long id,
        Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>> treeProcessor,
        PersistableBundle params) {

    public static <C> TreeProcessorDescriptionEntity of(final TreeProcessorDescription<C> treeProcessorDescription) {
        return new TreeProcessorDescriptionEntity(
                0,
                (Either) treeProcessorDescription.treeProcessor(),
                treeProcessorDescription.params());
    }

    public <C> TreeProcessorDescription<C> treeProcessorDescription() {
        return new TreeProcessorDescription<C>(
                (Either) treeProcessor(),
                params());
    }
}

