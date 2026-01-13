package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createGraph;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createSingleNodeGraph;

import androidx.preference.PreferenceFragmentCompat;

import com.google.common.graph.ImmutableValueGraph;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.Graphs;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PersistableBundleTestFactory;

@RunWith(RobolectricTestRunner.class)
public class PojoGraphToEntityGraphTransformerTest {

    @Test
    public void test_toPojoGraph_singleNodeGraph() {
        final Graphs graphs =
                createSingleNodeGraph(
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
                                "graph-screen1",
                                "graph-screen2"));
        test_toEntityGraph(
                graphs.pojoTree(),
                graphs.entityTreeAndDbDataProvider());
    }

    @Test
    public void test_toPojoGraph_multiNodeGraph() {
        final Graphs graphs =
                createGraph(
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
                                "graph-screen1",
                                "graph-screen2"));
        test_toEntityGraph(
                graphs.pojoTree(),
                graphs.entityTreeAndDbDataProvider());
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