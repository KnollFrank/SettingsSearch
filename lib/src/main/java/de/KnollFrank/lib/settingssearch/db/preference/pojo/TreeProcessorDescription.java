package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.PersistableBundle;

import com.codepoetics.ambivalence.Either;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;

public record TreeProcessorDescription<C>(
        Either<Class<? extends SearchablePreferenceScreenTreeCreator<C>>, Class<? extends SearchablePreferenceScreenTreeTransformer<C>>> treeProcessor,
        PersistableBundle params) {
}
