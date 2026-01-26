package de.KnollFrank.lib.settingssearch.db.preference.db.transformer;

import com.codepoetics.ambivalence.Either;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeProcessorDescription;

public interface TreeProcessorFactory<C> {

    Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>> createTreeProcessor(TreeProcessorDescription<C> treeProcessorDescription);
}
