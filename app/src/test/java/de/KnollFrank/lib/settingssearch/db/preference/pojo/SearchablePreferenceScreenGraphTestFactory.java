package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableMap;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Pair;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PersistableBundleTestFactory;
import de.KnollFrank.lib.settingssearch.graph.Graph2POJOGraphTransformerTest;
import de.KnollFrank.lib.settingssearch.graph.Graph2POJOGraphTransformerTest.PreferenceFragmentWithSinglePreference;

public class SearchablePreferenceScreenGraphTestFactory {

    public record Data(String DST_PREFERENCE_ID,
                       String PREFERENCE_CONNECTING_SRC_2_DST_ID,
                       String PARENT_KEY,
                       String parentId,
                       String child1Id,
                       String child2Id,
                       String singleNodeScreenId,
                       String twoNodeScreen1Id,
                       String twoNodeScreen2Id) {
    }

    public record Graphs(
            GraphAndDbDataProvider entityGraphAndDbDataProvider,
            Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph) {
    }

    public static Graphs createSingleNodeGraph(final Class<? extends PreferenceFragmentCompat> host,
                                               final Locale locale,
                                               final Data data) {
        final String screenId = data.singleNodeScreenId();
        final SearchablePreferenceEntity preferenceConnectingSrc2Dst =
                new SearchablePreferenceEntity(
                        data.PREFERENCE_CONNECTING_SRC_2_DST_ID(),
                        "some key",
                        Optional.of("preference connected to TestPreferenceFragment"),
                        Optional.empty(),
                        Optional.empty(),
                        2131427444,
                        0,
                        Optional.of(Graph2POJOGraphTransformerTest.PreferenceFragmentWithSinglePreference.class.getName()),
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        screenId);
        final SearchablePreference preferencePojoConnectingSrc2Dst =
                new SearchablePreference(
                        data.PREFERENCE_CONNECTING_SRC_2_DST_ID(),
                        "some key",
                        Optional.of("preference connected to TestPreferenceFragment"),
                        Optional.empty(),
                        Optional.empty(),
                        2131427444,
                        0,
                        Optional.of(Graph2POJOGraphTransformerTest.PreferenceFragmentWithSinglePreference.class.getName()),
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        Set.of(),
                        Optional.empty());
        final SearchablePreferenceScreenGraphEntity graphEntity =
                new SearchablePreferenceScreenGraphEntity(
                        locale,
                        PersistableBundleTestFactory.createSomePersistableBundle());
        final Pair<Pair<SearchablePreferenceScreenEntity, DbDataProviderData>, SearchablePreferenceScreen> src =
                createSrc(
                        screenId,
                        preferenceConnectingSrc2Dst,
                        preferencePojoConnectingSrc2Dst,
                        host,
                        graphEntity.id(),
                        data);
        return new Graphs(
                new GraphAndDbDataProvider(
                        graphEntity,
                        DbDataProviderFactory.createDbDataProvider(
                                DbDataProviderDatas.merge(
                                        List.of(
                                                src.first().second(),
                                                DbDataProviderData
                                                        .builder()
                                                        .withNodesByGraph(
                                                                Map.of(
                                                                        graphEntity,
                                                                        Set.of(src.first().first())))
                                                        .withPredecessorByPreference(
                                                                Map.of(
                                                                        preferenceConnectingSrc2Dst,
                                                                        Optional.empty()))
                                                        .withChildrenByPreference(
                                                                Map.of(
                                                                        preferenceConnectingSrc2Dst,
                                                                        Set.of()))
                                                        .build())))),
                DefaultDirectedGraph
                        .<SearchablePreferenceScreen, SearchablePreferenceEdge>createBuilder(SearchablePreferenceEdge.class)
                        .addVertex(src.second())
                        .build());
    }

    public static Graphs createGraph(final Class<? extends PreferenceFragmentCompat> host,
                                     final Locale locale,
                                     final Data data) {
        final String screenId = data.twoNodeScreen1Id();
        final Class<? extends Fragment> fragment = PreferenceFragmentWithSinglePreference.class;
        final SearchablePreferenceEntity preferenceConnectingSrc2Dst =
                new SearchablePreferenceEntity(
                        data.PREFERENCE_CONNECTING_SRC_2_DST_ID(),
                        "some key",
                        Optional.of("preference connected to " + fragment.getSimpleName()),
                        Optional.empty(),
                        Optional.empty(),
                        2131427444,
                        0,
                        Optional.of(fragment.getName()),
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        screenId);
        final SearchablePreference preferencePojoConnectingSrc2Dst =
                new SearchablePreference(
                        data.PREFERENCE_CONNECTING_SRC_2_DST_ID(),
                        "some key",
                        Optional.of("preference connected to " + fragment.getSimpleName()),
                        Optional.empty(),
                        Optional.empty(),
                        2131427444,
                        0,
                        Optional.of(fragment.getName()),
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        Set.of(),
                        Optional.empty());
        final SearchablePreferenceScreenGraphEntity graphEntity =
                new SearchablePreferenceScreenGraphEntity(
                        locale,
                        PersistableBundleTestFactory.createSomePersistableBundle());
        final Pair<Pair<SearchablePreferenceScreenEntity, DbDataProviderData>, SearchablePreferenceScreen> src =
                createSrc(
                        screenId,
                        preferenceConnectingSrc2Dst,
                        preferencePojoConnectingSrc2Dst,
                        host,
                        graphEntity.id(),
                        data);
        final Pair<Pair<SearchablePreferenceScreenEntity, DbDataProviderData>, SearchablePreferenceScreen> dst =
                createDst(
                        preferenceConnectingSrc2Dst,
                        preferencePojoConnectingSrc2Dst,
                        graphEntity.id(),
                        data);
        return new Graphs(
                new GraphAndDbDataProvider(
                        graphEntity,
                        DbDataProviderFactory.createDbDataProvider(
                                DbDataProviderDatas.merge(
                                        List.of(
                                                src.first().second(),
                                                dst.first().second(),
                                                DbDataProviderData
                                                        .builder()
                                                        .withNodesByGraph(
                                                                Map.of(
                                                                        graphEntity,
                                                                        Set.of(
                                                                                src.first().first(),
                                                                                dst.first().first())))
                                                        .withPredecessorByPreference(
                                                                Map.of(
                                                                        preferenceConnectingSrc2Dst,
                                                                        Optional.empty()))
                                                        .withChildrenByPreference(
                                                                Map.of(
                                                                        preferenceConnectingSrc2Dst,
                                                                        Set.of()))
                                                        .build())))),
                DefaultDirectedGraph
                        .<SearchablePreferenceScreen, SearchablePreferenceEdge>createBuilder(SearchablePreferenceEdge.class)
                        .addEdge(
                                src.second(),
                                dst.second(),
                                new SearchablePreferenceEdge(preferencePojoConnectingSrc2Dst))
                        .build());
    }

    private static Pair<Pair<SearchablePreferenceScreenEntity, DbDataProviderData>, SearchablePreferenceScreen> createSrc(
            final String screenId,
            final SearchablePreferenceEntity preferenceConnectingSrc2Dst,
            final SearchablePreference preferencePojoConnectingSrc2Dst,
            final Class<? extends PreferenceFragmentCompat> host,
            final Locale graphId,
            final Data data) {
        final SearchablePreferenceEntity parent =
                new SearchablePreferenceEntity(
                        data.parentId(),
                        data.PARENT_KEY(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        15,
                        0,
                        Optional.empty(),
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        screenId);
        final SearchablePreferenceEntity child1 =
                new SearchablePreferenceEntity(
                        data.child1Id(),
                        "some child key 1",
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        16,
                        0,
                        Optional.empty(),
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        Optional.of(parent.id()),
                        Optional.empty(),
                        screenId);
        final SearchablePreference child1Pojo =
                new SearchablePreference(
                        data.child1Id(),
                        "some child key 1",
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        16,
                        0,
                        Optional.empty(),
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        Set.of(),
                        Optional.empty());
        final SearchablePreferenceEntity child2 =
                new SearchablePreferenceEntity(
                        data.child2Id(),
                        "some child key 2",
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        16,
                        0,
                        Optional.empty(),
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        Optional.of(parent.id()),
                        Optional.empty(),
                        screenId);
        final SearchablePreference child2Pojo =
                new SearchablePreference(
                        data.child2Id(),
                        "some child key 2",
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        16,
                        0,
                        Optional.empty(),
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        Set.of(),
                        Optional.empty());
        final SearchablePreference parentPojo =
                new SearchablePreference(
                        data.parentId(),
                        data.PARENT_KEY(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        15,
                        0,
                        Optional.empty(),
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        Set.of(child1Pojo, child2Pojo),
                        Optional.empty());
        final SearchablePreferenceScreenEntity searchablePreferenceScreen =
                new SearchablePreferenceScreenEntity(
                        screenId,
                        host,
                        Optional.of("screen title"),
                        Optional.of("screen summary"),
                        graphId);
        return Pair.create(
                Pair.create(
                        searchablePreferenceScreen,
                        new DbDataProviderDataBuilder()
                                .withAllPreferencesBySearchablePreferenceScreen(
                                        Map.of(
                                                searchablePreferenceScreen,
                                                Set.of(parent, child1, child2, preferenceConnectingSrc2Dst)))
                                .withHostByPreference(
                                        ImmutableMap
                                                .<SearchablePreferenceEntity, SearchablePreferenceScreenEntity>builder()
                                                .put(parent, searchablePreferenceScreen)
                                                .put(child1, searchablePreferenceScreen)
                                                .put(child2, searchablePreferenceScreen)
                                                .put(preferenceConnectingSrc2Dst, searchablePreferenceScreen)
                                                .build())
                                .withPredecessorByPreference(
                                        ImmutableMap
                                                .<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>>builder()
                                                .put(parent, Optional.empty())
                                                .put(child1, Optional.empty())
                                                .put(child2, Optional.empty())
                                                .build())
                                .withChildrenByPreference(
                                        ImmutableMap
                                                .<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>>builder()
                                                .put(parent, Set.of(child1, child2))
                                                .put(child1, Set.of())
                                                .put(child2, Set.of())
                                                .build())
                                .build()),
                new SearchablePreferenceScreen(
                        screenId,
                        host,
                        Optional.of("screen title"),
                        Optional.of("screen summary"),
                        Set.of(parentPojo, child1Pojo, child2Pojo, preferencePojoConnectingSrc2Dst)));
    }

    private static Pair<Pair<SearchablePreferenceScreenEntity, DbDataProviderData>, SearchablePreferenceScreen> createDst(
            final SearchablePreferenceEntity predecessor,
            final SearchablePreference predecessorPojo,
            final Locale graphId,
            final Data data) {
        final String screenId = data.twoNodeScreen2Id();
        final SearchablePreferenceEntity searchablePreference =
                new SearchablePreferenceEntity(
                        data.DST_PREFERENCE_ID(),
                        "some dst key",
                        Optional.of("some title for dst key"),
                        Optional.empty(),
                        Optional.empty(),
                        16,
                        0,
                        Optional.empty(),
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of(predecessor.id()),
                        screenId);
        final SearchablePreference searchablePreferencePojo =
                new SearchablePreference(
                        data.DST_PREFERENCE_ID(),
                        "some dst key",
                        Optional.of("some title for dst key"),
                        Optional.empty(),
                        Optional.empty(),
                        16,
                        0,
                        Optional.empty(),
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        Set.of(),
                        Optional.of(predecessorPojo));
        final SearchablePreferenceScreenEntity searchablePreferenceScreen =
                new SearchablePreferenceScreenEntity(
                        screenId,
                        PreferenceFragmentWithSinglePreference.class,
                        Optional.of("screen title"),
                        Optional.of("screen summary"),
                        graphId);
        return Pair.create(
                Pair.create(
                        searchablePreferenceScreen,
                        new DbDataProviderDataBuilder()
                                .withAllPreferencesBySearchablePreferenceScreen(
                                        Map.of(
                                                searchablePreferenceScreen,
                                                Set.of(searchablePreference)))
                                .withHostByPreference(
                                        Map.of(
                                                searchablePreference,
                                                searchablePreferenceScreen))
                                .withPredecessorByPreference(
                                        Map.of(
                                                searchablePreference,
                                                Optional.of(predecessor)))
                                .withChildrenByPreference(
                                        Map.of(
                                                searchablePreference,
                                                Set.of()))
                                .build()),
                new SearchablePreferenceScreen(
                        screenId,
                        Graph2POJOGraphTransformerTest.PreferenceFragmentWithSinglePreference.class,
                        Optional.of("screen title"),
                        Optional.of("screen summary"),
                        Set.of(searchablePreferencePojo)));
    }
}
