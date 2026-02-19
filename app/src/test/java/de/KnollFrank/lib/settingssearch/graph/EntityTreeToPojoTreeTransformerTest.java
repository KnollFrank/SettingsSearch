package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.PreferenceFragmentClassOfActivityTestFactory.createSomePreferenceFragmentClassOfActivity;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createGraph;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createSingleNodeGraph;

import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.common.LanguageCode;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.Trees;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeAndDbDataProvider;

@RunWith(RobolectricTestRunner.class)
@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class EntityTreeToPojoTreeTransformerTest {

    @Test
    public void test_toPojoGraph_singleNodeGraph() {
        final Trees trees =
                createSingleNodeGraph(
                        createSomePreferenceFragmentClassOfActivity(),
                        LanguageCode.from(Locale.GERMAN),
                        new SearchablePreferenceScreenGraphTestFactory.Data(
                                "5",
                                "4",
                                "parentKey",
                                "1",
                                "2",
                                "3",
                                "singleNodeGraph-screen1",
                                "graph-screen1",
                                "graph-screen2"));
        test_toPojoGraph(
                trees.entityTreeAndDbDataProvider(),
                trees.pojoTree());
    }

    @Test
    public void test_toPojoGraph_multiNodeGraph() {
        final Trees trees =
                createGraph(
                        createSomePreferenceFragmentClassOfActivity(),
                        LanguageCode.from(Locale.GERMAN),
                        new SearchablePreferenceScreenGraphTestFactory.Data(
                                "5",
                                "4",
                                "parentKey",
                                "1",
                                "2",
                                "3",
                                "singleNodeGraph-screen1",
                                "graph-screen1",
                                "graph-screen2"));
        test_toPojoGraph(
                trees.entityTreeAndDbDataProvider(),
                trees.pojoTree());
    }

    private static void test_toPojoGraph(final TreeAndDbDataProvider entityTreeAndDbDataProvider,
                                         final Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> pojoTreeExpected) {
        // When
        final Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> pojoGraphActual =
                EntityTreeToPojoTreeTransformer.toPojoTree(
                        entityTreeAndDbDataProvider.asGraph(),
                        entityTreeAndDbDataProvider.dbDataProvider());

        // Then
        assertThat(pojoGraphActual, is(pojoTreeExpected));
    }
}