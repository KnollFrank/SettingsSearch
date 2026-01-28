package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.PersistableBundle;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;

public record TreeCreatorDescription<C>(
        Class<? extends SearchablePreferenceScreenTreeCreator<C>> treeCreator,
        PersistableBundle params) {
}
