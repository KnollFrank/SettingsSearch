package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import com.codepoetics.ambivalence.Either;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TreeProcessorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeProcessorDescription;

public class TreeProcessorDescriptionConverter<C> implements Converter<TreeProcessorDescription<C>, Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>>> {

    private final TreeProcessorFactory<C> treeProcessorFactory;

    public TreeProcessorDescriptionConverter(final TreeProcessorFactory<C> treeProcessorFactory) {
        this.treeProcessorFactory = treeProcessorFactory;
    }

    @Override
    public Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>> convertForward(final TreeProcessorDescription<C> treeProcessorDescription) {
        return treeProcessorDescription
                .getDescriptions()
                .map(treeProcessorFactory::createTreeCreator, treeProcessorFactory::createTreeTransformer);
    }

    @Override
    public TreeProcessorDescription<C> convertBackward(final Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>> treeProcessor) {
        return treeProcessor.join(
                this::getTreeProcessorDescription,
                this::getTreeProcessorDescription);
    }

    private TreeProcessorDescription<C> getTreeProcessorDescription(final SearchablePreferenceScreenTreeCreator<C> treeCreator) {
        return new TreeProcessorDescription<>(
                Either.ofLeft((Class<? extends SearchablePreferenceScreenTreeCreator<C>>) treeCreator.getClass()),
                treeCreator.getParams());
    }

    private TreeProcessorDescription<C> getTreeProcessorDescription(final SearchablePreferenceScreenTreeTransformer<C> treeTransformer) {
        return new TreeProcessorDescription<>(
                Either.ofRight((Class<? extends SearchablePreferenceScreenTreeTransformer<C>>) treeTransformer.getClass()),
                treeTransformer.getParams());
    }
}
