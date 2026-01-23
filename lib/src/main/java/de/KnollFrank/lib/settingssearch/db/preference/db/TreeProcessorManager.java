package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;

import com.codepoetics.ambivalence.Either;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

class TreeProcessorManager<C> {

    private final ConfigurationBundleConverter<C> configurationBundleConverter;
    // FK-TODO: treeProcessors in der Suchdatenbank speichern
    // FK-TODO: use Queue instead of List?
    private final List<Either<SearchablePreferenceScreenTreeTransformer<C>, SearchablePreferenceScreenTreeCreator<C>>> treeProcessors = new ArrayList<>();

    public TreeProcessorManager(final ConfigurationBundleConverter<C> configurationBundleConverter) {
        this.configurationBundleConverter = configurationBundleConverter;
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

    public List<SearchablePreferenceScreenTree<PersistableBundle>> applyTreeProcessorsToTrees(
            final List<SearchablePreferenceScreenTree<PersistableBundle>> trees,
            final C configuration,
            final FragmentActivity activityContext) {
        final List<SearchablePreferenceScreenTree<PersistableBundle>> transformedTrees =
                trees
                        .stream()
                        .map(tree -> applyTreeProcessorsToTree(tree, configuration, activityContext))
                        .collect(Collectors.toList());
        removeTreeProcessors();
        return transformedTrees;
    }

    private SearchablePreferenceScreenTree<PersistableBundle> applyTreeProcessorsToTree(
            final SearchablePreferenceScreenTree<PersistableBundle> tree,
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

    private SearchablePreferenceScreenTree<PersistableBundle> applyTreeProcessorToTree(
            final Either<SearchablePreferenceScreenTreeTransformer<C>, SearchablePreferenceScreenTreeCreator<C>> treeProcessor,
            final SearchablePreferenceScreenTree<PersistableBundle> tree,
            final C configuration,
            final FragmentActivity activityContext) {
        return treeProcessor.join(
                treeTransformer -> treeTransformer.transformTree(tree, configuration, activityContext),
                treeCreator ->
                        new SearchablePreferenceScreenTree<>(
                                treeCreator.createTree(
                                        tree.locale(),
                                        configuration,
                                        activityContext).tree(),
                                tree.locale(),
                                configurationBundleConverter.convertForward(configuration)));
    }
}
