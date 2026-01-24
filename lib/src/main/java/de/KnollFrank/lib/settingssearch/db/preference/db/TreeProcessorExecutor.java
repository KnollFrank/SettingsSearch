package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;

import com.codepoetics.ambivalence.Either;
import com.google.common.graph.ImmutableValueGraph;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConfigurationBundleConverter;

public class TreeProcessorExecutor<C> {

    private final ConfigurationBundleConverter<C> configurationBundleConverter;

    public TreeProcessorExecutor(final ConfigurationBundleConverter<C> configurationBundleConverter) {
        this.configurationBundleConverter = configurationBundleConverter;
    }

    public List<SearchablePreferenceScreenTree<PersistableBundle>> applyTreeProcessorsToTrees(
            final List<Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>>> treeProcessors,
            final List<SearchablePreferenceScreenTree<PersistableBundle>> trees,
            final C configuration,
            final FragmentActivity activityContext) {
        return this
                .createProcessorExecutionContext(configuration, activityContext)
                .applyTreeProcessorsToTrees(treeProcessors, trees);
    }

    private ProcessorExecutionContext<C> createProcessorExecutionContext(
            final C configuration,
            final FragmentActivity activityContext) {
        return new ProcessorExecutionContext<>(configuration, configurationBundleConverter, activityContext);
    }

    private static class ProcessorExecutionContext<C> {

        private final C configuration;
        private final ConfigurationBundleConverter<C> configurationBundleConverter;
        private final FragmentActivity activityContext;

        public ProcessorExecutionContext(final C configuration,
                                         final ConfigurationBundleConverter<C> configurationBundleConverter,
                                         final FragmentActivity activityContext) {
            this.configuration = configuration;
            this.configurationBundleConverter = configurationBundleConverter;
            this.activityContext = activityContext;
        }

        public List<SearchablePreferenceScreenTree<PersistableBundle>> applyTreeProcessorsToTrees(
                final List<Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>>> treeProcessors,
                final List<SearchablePreferenceScreenTree<PersistableBundle>> trees) {
            return trees
                    .stream()
                    .map(tree -> applyTreeProcessorsToTree(treeProcessors, tree))
                    .collect(Collectors.toList());
        }

        private SearchablePreferenceScreenTree<PersistableBundle> applyTreeProcessorsToTree(
                final List<Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>>> treeProcessors,
                final SearchablePreferenceScreenTree<PersistableBundle> tree) {
            return treeProcessors
                    .stream()
                    .reduce(
                            tree,
                            (currentTree, treeProcessor) -> applyTreeProcessorToTree(treeProcessor, currentTree),
                            (tree1, tree2) -> {
                                throw new UnsupportedOperationException("Parallel stream not supported");
                            });
        }

        private SearchablePreferenceScreenTree<PersistableBundle> applyTreeProcessorToTree(
                final Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>> treeProcessor,
                final SearchablePreferenceScreenTree<PersistableBundle> tree) {
            return new SearchablePreferenceScreenTree<>(
                    _applyTreeProcessorToTree(treeProcessor, tree),
                    tree.locale(),
                    configurationBundleConverter.convertForward(configuration));
        }

        @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
        private Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> _applyTreeProcessorToTree(
                final Either<SearchablePreferenceScreenTreeCreator<C>, SearchablePreferenceScreenTreeTransformer<C>> treeProcessor,
                final SearchablePreferenceScreenTree<PersistableBundle> tree) {
            return treeProcessor.join(
                    treeCreator ->
                            treeCreator.createTree(
                                    tree.locale(),
                                    configuration,
                                    activityContext),
                    treeTransformer ->
                            treeTransformer.transformSearchablePreferenceScreenTree(
                                    tree.mapConfiguration(configurationBundleConverter::convertBackward),
                                    configuration,
                                    activityContext));
        }
    }
}
