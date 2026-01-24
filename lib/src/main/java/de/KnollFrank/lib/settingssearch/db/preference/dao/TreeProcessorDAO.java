package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.codepoetics.ambivalence.Either;

import java.util.ArrayList;
import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;

public class TreeProcessorDAO<C> {

    // FK-TODO: treeProcessors in der Suchdatenbank speichern
    // FK-TODO: use Queue instead of List?
    private final List<Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>>> treeProcessors = new ArrayList<>();

    public List<Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>>> getTreeProcessors() {
        return treeProcessors;
    }

    public void addTreeCreator(final SearchablePreferenceScreenTreeCreator<C> treeCreator) {
        removeTreeProcessors();
        treeProcessors.add(Either.ofLeft(treeCreator));
    }

    public void addTreeTransformer(final SearchablePreferenceScreenTreeTransformer<C> treeTransformer) {
        treeProcessors.add(Either.ofRight(treeTransformer));
    }

    public void removeTreeProcessors() {
        treeProcessors.clear();
    }

    public boolean hasTreeProcessors() {
        return !treeProcessors.isEmpty();
    }
}
