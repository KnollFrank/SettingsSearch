package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.codepoetics.ambivalence.Either;

import java.util.ArrayList;
import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TreeProcessorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeProcessorDescription;

public class TreeProcessorDAO<C> {

    private final TreeProcessorFactory<C> treeProcessorFactory;
    private final List<TreeProcessorDescription<C>> treeProcessorDescriptions = new ArrayList<>();

    public TreeProcessorDAO(final TreeProcessorFactory<C> treeProcessorFactory) {
        this.treeProcessorFactory = treeProcessorFactory;
    }

    public List<Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>>> getTreeProcessors() {
        return treeProcessorDescriptions
                .stream()
                .map(treeProcessorFactory::createTreeProcessor)
                .toList();
    }

    public void addTreeCreator(final SearchablePreferenceScreenTreeCreator<C> treeCreator) {
        removeTreeProcessors();
        treeProcessorDescriptions.add(getTreeProcessorDescription(treeCreator));
    }

    public void addTreeTransformer(final SearchablePreferenceScreenTreeTransformer<C> treeTransformer) {
        treeProcessorDescriptions.add(getTreeProcessorDescription(treeTransformer));
    }

    public void removeTreeProcessors() {
        treeProcessorDescriptions.clear();
    }

    public boolean hasTreeProcessors() {
        return !treeProcessorDescriptions.isEmpty();
    }

    private static <C> TreeProcessorDescription<C> getTreeProcessorDescription(final SearchablePreferenceScreenTreeCreator<C> treeCreator) {
        return new TreeProcessorDescription<>(
                Either.ofLeft((Class<? extends SearchablePreferenceScreenTreeCreator<C>>) treeCreator.getClass()),
                treeCreator.getParams());
    }

    private static <C> TreeProcessorDescription<C> getTreeProcessorDescription(final SearchablePreferenceScreenTreeTransformer<C> treeTransformer) {
        return new TreeProcessorDescription<>(
                Either.ofRight((Class<? extends SearchablePreferenceScreenTreeTransformer<C>>) treeTransformer.getClass()),
                treeTransformer.getParams());
    }
}
