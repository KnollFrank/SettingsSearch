package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.codepoetics.ambivalence.Either;

import java.util.ArrayList;
import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeProcessorDescription;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.TreeProcessorDescriptionConverter;

public class TreeProcessorDAO<C> {

    private final TreeProcessorDescriptionConverter<C> treeProcessorDescriptionConverter;
    private final List<TreeProcessorDescription<C>> treeProcessorDescriptions = new ArrayList<>();

    public TreeProcessorDAO(final TreeProcessorDescriptionConverter<C> treeProcessorDescriptionConverter) {
        this.treeProcessorDescriptionConverter = treeProcessorDescriptionConverter;
    }

    public List<Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>>> getTreeProcessors() {
        return treeProcessorDescriptions
                .stream()
                .map(treeProcessorDescriptionConverter::convertForward)
                .toList();
    }

    public void addTreeCreator(final SearchablePreferenceScreenTreeCreator<C> treeCreator) {
        removeTreeProcessors();
        treeProcessorDescriptions.add(treeProcessorDescriptionConverter.convertBackward(Either.ofLeft(treeCreator)));
    }

    public void addTreeTransformer(final SearchablePreferenceScreenTreeTransformer<C> treeTransformer) {
        treeProcessorDescriptions.add(treeProcessorDescriptionConverter.convertBackward(Either.ofRight(treeTransformer)));
    }

    public void removeTreeProcessors() {
        treeProcessorDescriptions.clear();
    }

    public boolean hasTreeProcessors() {
        return !treeProcessorDescriptions.isEmpty();
    }
}
