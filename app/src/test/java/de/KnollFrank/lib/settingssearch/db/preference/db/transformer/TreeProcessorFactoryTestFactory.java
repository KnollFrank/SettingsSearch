package de.KnollFrank.lib.settingssearch.db.preference.db.transformer;

import com.codepoetics.ambivalence.Either;

import de.KnollFrank.lib.settingssearch.common.Functions;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeProcessorDescription;

public class TreeProcessorFactoryTestFactory {

    public static <C> TreeProcessorFactory<C> createTreeProcessorFactory() {
        return new TreeProcessorFactory<>() {

            @Override
            public Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>> createTreeProcessor(final TreeProcessorDescription<C> treeProcessorDescription) {
                return treeProcessorDescription
                        .treeProcessor()
                        .map(Functions.constant(new TestTreeCreator<>()),
                             Functions.constant(new TestTreeTransformer<>()));
            }
        };
    }
}
