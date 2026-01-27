package de.KnollFrank.lib.settingssearch.db.preference.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.codepoetics.ambivalence.Either;
import com.google.common.collect.Iterables;
import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Functions;
import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TestTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TestTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TreeProcessorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeProcessorDescription;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PersistableBundleTestFactory;
import de.KnollFrank.settingssearch.Configuration;
import de.KnollFrank.settingssearch.ConfigurationBundleConverter;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenTreeRepositoryTest extends PreferencesRoomDatabaseTest {

    @Test
    public void shouldApplyTreeProcessors() {
        // Given
        final SearchablePreferenceScreenTreeRepository<Configuration> repository = createTreeRepository();
        final SearchablePreferenceScreenTree<PersistableBundle> initialTree = createInitialTree();
        final String transformedTitle = "transformed " + initialTree.tree().rootNode().title().orElseThrow();
        preferencesRoomDatabase.searchablePreferenceScreenTreeDao().persistOrReplace(initialTree);

        // When
        repository.addTreeTransformer(new TitleChangingTransformer(transformedTitle));

        // Then
        assertThat(repository.getTreeProcessors(), hasSize(1));

        // When
        final SearchablePreferenceScreenTree<PersistableBundle> transformedTree =
                Iterables.getOnlyElement(
                        repository.loadAll(
                                PersistableBundleTestFactory.createSomeConfiguration(),
                                mock(FragmentActivity.class)));

        // Then
        assertThat(repository.getTreeProcessors(), hasSize(0));
        assertThat(transformedTree.tree().rootNode().title().orElseThrow(), is(transformedTitle));
    }

    @Test
    public void whenTransactionFails_databaseShouldRollback() {
        // Given
        final SearchablePreferenceScreenTreeRepository<Configuration> repository = createTreeRepository();
        final SearchablePreferenceScreenTree<PersistableBundle> initialTree = createInitialTree();
        preferencesRoomDatabase.searchablePreferenceScreenTreeDao().persistOrReplace(initialTree);
        repository.addTreeTransformer(new TitleChangingTransformer("transformed " + initialTree.tree().rootNode().title().orElseThrow()));
        repository.addTreeTransformer(new ExceptionThrowingTransformer());
        assertThat(repository.getTreeProcessors(), hasSize(2));

        // When
        try {
            repository.loadAll(PersistableBundleTestFactory.createSomeConfiguration(), mock(FragmentActivity.class));
            fail("Expected TransformerException was not thrown.");
        } catch (final TransformerException e) {
            // Then
            assertThat(e.getMessage(), is(ExceptionThrowingTransformer.EXCEPTION_MESSAGE));
        }

        // Then
        assertThat(repository.getTreeProcessors(), hasSize(2));
        assertThat(
                preferencesRoomDatabase
                        .searchablePreferenceScreenTreeDao()
                        .findTreeById(initialTree.locale())
                        .orElseThrow()
                        .tree(),
                is(initialTree.tree()));
    }

    private SearchablePreferenceScreenTree<PersistableBundle> createInitialTree() {
        return new SearchablePreferenceScreenTree<>(
                SearchablePreferenceScreenGraphTestFactory
                        .createSingleNodeGraph(
                                PreferenceFragmentCompat.class,
                                Locale.GERMAN,
                                new SearchablePreferenceScreenGraphTestFactory.Data(
                                        "5",
                                        "4",
                                        "parentKey",
                                        "1",
                                        "2",
                                        "3",
                                        "singleNodeGraph-screen1",
                                        "tree-screen1",
                                        "tree-screen2"))
                        .pojoTree(),
                Locale.GERMAN,
                PersistableBundleTestFactory.createSomePersistableBundle());
    }

    private SearchablePreferenceScreenTreeRepository<Configuration> createTreeRepository() {
        return new SearchablePreferenceScreenTreeRepository<>(
                preferencesRoomDatabase,
                preferencesRoomDatabase.searchablePreferenceScreenTreeDao(),
                TreeProcessorManagerFactory.createTreeProcessorManager(
                        preferencesRoomDatabase.treeProcessorDescriptionEntityDao(),
                        new TreeProcessorFactory<Configuration>() {

                            @Override
                            public Either<SearchablePreferenceScreenTreeCreator<Configuration>, SearchablePreferenceScreenTreeTransformer<Configuration>> createTreeProcessor(final TreeProcessorDescription<Configuration> treeProcessorDescription) {
                                return treeProcessorDescription
                                        .treeProcessor()
                                        .map(Functions.constant(new TestTreeCreator<>()),
                                             treeTransformerClass -> {
                                                 if (ExceptionThrowingTransformer.class.equals(treeTransformerClass)) {
                                                     return new ExceptionThrowingTransformer();
                                                 }
                                                 if (TitleChangingTransformer.class.equals(treeTransformerClass)) {
                                                     final String newTitle = treeProcessorDescription.params().getString("newTitle");
                                                     return new TitleChangingTransformer(newTitle);
                                                 }
                                                 return new TestTreeTransformer<>();
                                             });
                            }
                        },
                        new ConfigurationBundleConverter()));
    }

    private static class TransformerException extends RuntimeException {

        public TransformerException(final String message) {
            super(message);
        }
    }

    private static class ExceptionThrowingTransformer implements SearchablePreferenceScreenTreeTransformer<Configuration> {

        private static final String EXCEPTION_MESSAGE = "Transaction test exception";

        @Override
        @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
        public Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> transformSearchablePreferenceScreenTree(
                final SearchablePreferenceScreenTree<Configuration> searchablePreferenceScreenTree,
                final Configuration targetConfiguration,
                final FragmentActivity activityContext) {
            throw new TransformerException(EXCEPTION_MESSAGE);
        }

        @Override
        public PersistableBundle getParams() {
            return new PersistableBundle();
        }
    }

    private static class TitleChangingTransformer implements SearchablePreferenceScreenTreeTransformer<Configuration> {

        private final String newTitle;

        public TitleChangingTransformer(final String newTitle) {
            this.newTitle = newTitle;
        }

        @Override
        @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
        public Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> transformSearchablePreferenceScreenTree(
                final SearchablePreferenceScreenTree<Configuration> searchablePreferenceScreenTree,
                final Configuration targetConfiguration,
                final FragmentActivity activityContext) {
            return new Tree<>(
                    Graphs
                            .<SearchablePreferenceScreen, SearchablePreference>directedImmutableValueGraphBuilder()
                            .addNode(adaptTitle(searchablePreferenceScreenTree.tree().rootNode(), this.newTitle))
                            .build());
        }

        private static SearchablePreferenceScreen adaptTitle(final SearchablePreferenceScreen searchablePreferenceScreen,
                                                             final String title) {
            return new SearchablePreferenceScreen(
                    searchablePreferenceScreen.id(),
                    searchablePreferenceScreen.host(),
                    Optional.of(title),
                    searchablePreferenceScreen.summary(),
                    searchablePreferenceScreen.allPreferencesOfPreferenceHierarchy());
        }

        @Override
        public PersistableBundle getParams() {
            final PersistableBundle params = new PersistableBundle();
            params.putString("newTitle", this.newTitle);
            return params;
        }
    }
}
