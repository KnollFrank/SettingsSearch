package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.PersistableBundle;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;

public record TreeTransformerDescription<C>(
        Class<? extends SearchablePreferenceScreenTreeTransformer<C>> treeTransformer,
        PersistableBundle params) {
}
