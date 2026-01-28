package de.KnollFrank.lib.settingssearch.db.preference.db.transformer;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeCreatorDescription;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeTransformerDescription;

public interface TreeProcessorFactory<C> {

    SearchablePreferenceScreenTreeCreator<C> createTreeCreator(TreeCreatorDescription<C> treeCreatorDescription);

    SearchablePreferenceScreenTreeTransformer<C> createTreeTransformer(TreeTransformerDescription<C> treeTransformerDescription);
}
