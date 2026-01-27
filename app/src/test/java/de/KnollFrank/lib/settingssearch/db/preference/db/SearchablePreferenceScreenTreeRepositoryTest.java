package de.KnollFrank.lib.settingssearch.db.preference.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.codepoetics.ambivalence.Either;
import com.google.common.graph.ImmutableValueGraph;

import org.junit.Before;
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
// FK-TODO: refine and refactor
public class SearchablePreferenceScreenTreeRepositoryTest extends PreferencesRoomDatabaseTest {

    private SearchablePreferenceScreenTreeRepository<Configuration> repository;
    private TreeProcessorManager<Configuration> treeProcessorManager;

    @Before
    public void setUp() {
        treeProcessorManager =
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
                        new ConfigurationBundleConverter());
        repository = createTreeRepository(treeProcessorManager);
    }

    @Test
    public void whenTransactionSucceeds_databaseShouldBeUpdatedAndProcessorsRemoved() {
        // Arrange: Define the transformation
        final String transformedTitle = "Transformed Title";

        // Arrange: Persist an initial tree
        final SearchablePreferenceScreenTree<PersistableBundle> initialTree = createInitialTree();
        final Locale treeId = initialTree.locale();
        preferencesRoomDatabase.searchablePreferenceScreenTreeDao().persistOrReplace(initialTree);
        assertThat(preferencesRoomDatabase.searchablePreferenceScreenTreeDao().loadAll().size(), is(1));

        // Arrange: Add a transformer that will perform the title change
        repository.addTreeTransformer(new TitleChangingTransformer(transformedTitle));
        assertThat(treeProcessorManager.hasTreeProcessors(), is(true));

        // Act: Trigger the transaction
        repository.loadAll(PersistableBundleTestFactory.createSomeConfiguration(), mock(FragmentActivity.class));

        // Assert: The tree processors SHOULD have been removed, because the transaction succeeded.
        assertThat(treeProcessorManager.hasTreeProcessors(), is(false));

        // Assert: The tree should have been transformed.
        final SearchablePreferenceScreenTree<PersistableBundle> transformedTree =
                preferencesRoomDatabase
                        .searchablePreferenceScreenTreeDao()
                        .findTreeById(treeId)
                        .orElseThrow();
        final String actualTitle = transformedTree.tree().rootNode().title().orElseThrow();
        assertThat(actualTitle, is(transformedTitle));

        // Assert: The tree count remains the same (it's replaced).
        assertThat(preferencesRoomDatabase.searchablePreferenceScreenTreeDao().loadAll().size(), is(1));
    }

    @Test
    public void whenTransactionFails_databaseShouldRollback() {
        // Arrange: Persist an initial tree and check it's there.
        final SearchablePreferenceScreenTree<PersistableBundle> initialTree = createInitialTree();
        preferencesRoomDatabase.searchablePreferenceScreenTreeDao().persistOrReplace(initialTree);
        final int initialCount = preferencesRoomDatabase.searchablePreferenceScreenTreeDao().loadAll().size();
        assertThat(initialCount, is(1));

        // Arrange: Add the misbehaving transformer and check it's there.
        repository.addTreeTransformer(new ExceptionThrowingTransformer());
        assertThat(treeProcessorManager.hasTreeProcessors(), is(true));

        // Act: Trigger the transaction, which is expected to fail.
        try {
            repository.loadAll(PersistableBundleTestFactory.createSomeConfiguration(), mock(FragmentActivity.class));
            fail("Expected RuntimeException was not thrown.");
        } catch (final MyException e) {
            // Assert: Verify it's the exception we expect.
            assertThat(e.getMessage(), is(ExceptionThrowingTransformer.EXCEPTION_MESSAGE));
        }

        // Assert: Check if the database state is rolled back.
        // The initial tree should still be there, and no new trees added.
        assertThat(preferencesRoomDatabase.searchablePreferenceScreenTreeDao().loadAll().size(), is(initialCount));

        // The tree processors should NOT have been removed, because the transaction failed before commit.
        assertThat(treeProcessorManager.hasTreeProcessors(), is(true));
    }

    private SearchablePreferenceScreenTree<PersistableBundle> createInitialTree() {
        return new SearchablePreferenceScreenTree<>(
                SearchablePreferenceScreenGraphTestFactory
                        .createGraph(
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

    private SearchablePreferenceScreenTreeRepository<Configuration> createTreeRepository(
            final TreeProcessorManager<Configuration> treeProcessorManager) {
        return new SearchablePreferenceScreenTreeRepository<>(
                preferencesRoomDatabase,
                preferencesRoomDatabase.searchablePreferenceScreenTreeDao(),
                treeProcessorManager);
    }

    private static class MyException extends RuntimeException {

        public MyException() {
        }

        public MyException(final String message) {
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
            throw new MyException(EXCEPTION_MESSAGE);
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
