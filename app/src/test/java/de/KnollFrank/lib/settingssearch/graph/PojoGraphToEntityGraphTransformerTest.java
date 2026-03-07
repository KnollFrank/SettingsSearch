package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.PreferenceFragmentClassOfActivityTestFactory.createSomePreferenceFragmentClassOfActivity;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createGraph;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createSingleNodeGraph;

import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.Trees;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PersistableBundleTestFactory;

@RunWith(RobolectricTestRunner.class)
public class PojoGraphToEntityGraphTransformerTest {

    @Test
    public void test_toPojoGraph_singleNodeGraph() {
        final Trees trees =
                createSingleNodeGraph(
                        createSomePreferenceFragmentClassOfActivity(),
                        Locale.GERMAN,
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
        test_toEntityGraph(
                trees.pojoTree(),
                trees.entityTreeAndDbDataProvider());
    }

    @Test
    public void test_toPojoGraph_multiNodeGraph() {
        final Trees trees =
                createGraph(
                        createSomePreferenceFragmentClassOfActivity(),
                        Locale.GERMAN,
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
        test_toEntityGraph(
                trees.pojoTree(),
                trees.entityTreeAndDbDataProvider());
    }

    private static void test_toEntityGraph(@SuppressWarnings({"UnstableApiUsage", "NullableProblems"}) final Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> pojoGraph,
                                           final TreeAndDbDataProvider entityTreeAndDbDataProviderExpected) {
        // When
        final TreeAndDbDataProvider entityTreeAndDbDataProviderActual =
                PojoGraphToEntityGraphTransformer.toEntityGraph(
                        pojoGraph,
                        Locale.GERMAN,
                        PersistableBundleTestFactory.createSomePersistableBundle());

        // Then
        EntityGraphEquality.assertActualEqualsExpected(
                entityTreeAndDbDataProviderActual,
                entityTreeAndDbDataProviderExpected);
    }
}