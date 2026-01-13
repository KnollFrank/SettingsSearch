package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import com.codepoetics.ambivalence.Either;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.graph.ComputePreferencesListener;

class TreeProcessorManager<C> {

    private final ComputePreferencesListener computePreferencesListener;
    // FK-TODO: treeProcessors in der Suchdatenbank speichern
    // FK-TODO: use Queue instead of List?
    private final List<Either<SearchablePreferenceScreenTreeTransformer<C>, SearchablePreferenceScreenTreeCreator<C>>> treeProcessors = new ArrayList<>();

    public TreeProcessorManager(final ComputePreferencesListener computePreferencesListener) {
        this.computePreferencesListener = computePreferencesListener;
    }

    public void addTreeTransformer(final SearchablePreferenceScreenTreeTransformer<C> treeTransformer) {
        treeProcessors.add(Either.ofLeft(treeTransformer));
    }

    public void addTreeCreator(final SearchablePreferenceScreenTreeCreator<C> treeCreator) {
        removeTreeProcessors();
        treeProcessors.add(Either.ofRight(treeCreator));
    }

    public void removeTreeProcessors() {
        treeProcessors.clear();
    }

    public boolean hasTreeProcessors() {
        return !treeProcessors.isEmpty();
    }

    public List<SearchablePreferenceScreenTree> applyTreeProcessorsToTrees(
            final List<SearchablePreferenceScreenTree> trees,
            final C configuration,
            final FragmentActivity activityContext) {
        computePreferencesListener.onStartComputePreferences();
        final List<SearchablePreferenceScreenTree> transformedTrees =
                trees
                        .stream()
                        .map(tree -> applyTreeProcessorsToTree(tree, configuration, activityContext))
                        .collect(Collectors.toList());
        removeTreeProcessors();
        computePreferencesListener.onFinishComputePreferences();
        return transformedTrees;
    }

    private SearchablePreferenceScreenTree applyTreeProcessorsToTree(
            final SearchablePreferenceScreenTree tree,
            final C configuration,
            final FragmentActivity activityContext) {
        return treeProcessors
                .stream()
                .reduce(
                        tree,
                        (currentTree, treeProcessor) ->
                                applyTreeProcessorToTree(
                                        treeProcessor,
                                        currentTree,
                                        configuration,
                                        activityContext),
                        (tree1, tree2) -> {
                            throw new UnsupportedOperationException("Parallel stream not supported");
                        });
    }

    private static <C> SearchablePreferenceScreenTree applyTreeProcessorToTree(
            final Either<SearchablePreferenceScreenTreeTransformer<C>, SearchablePreferenceScreenTreeCreator<C>> treeProcessor,
            final SearchablePreferenceScreenTree tree,
            final C configuration,
            final FragmentActivity activityContext) {
        return treeProcessor.join(
                treeTransformer -> treeTransformer.transformTree(tree, configuration, activityContext),
                treeCreator -> treeCreator.createTree(tree.locale(), configuration, activityContext));
    }
}
