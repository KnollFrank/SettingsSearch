package de.KnollFrank.lib.settingssearch.db.preference.db.transformer;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeCreatorDescription;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeTransformerDescription;

public class TreeProcessorFactoryTestFactory {

    public static <C> TreeProcessorFactory<C> createTreeProcessorFactory() {
        return new TreeProcessorFactory<>() {

            @Override
            public SearchablePreferenceScreenTreeCreator<C> createTreeCreator(final TreeCreatorDescription<C> treeCreatorDescription) {
                return new TestTreeCreator<>();
            }

            @Override
            public SearchablePreferenceScreenTreeTransformer<C> createTreeTransformer(final TreeTransformerDescription<C> treeTransformerDescription) {
                return new TestTreeTransformer<>();
            }
        };
    }
}
