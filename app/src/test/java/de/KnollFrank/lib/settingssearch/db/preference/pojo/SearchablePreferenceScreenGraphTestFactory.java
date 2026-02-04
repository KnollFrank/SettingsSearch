package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.PersistableBundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableMap;
import com.google.common.graph.ImmutableValueGraph;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.ActivityDescription;
import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.common.Pair;
import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PersistableBundleTestFactory;
import de.KnollFrank.lib.settingssearch.graph.TreeToPojoTreeTransformerTest;
import de.KnollFrank.lib.settingssearch.graph.TreeToPojoTreeTransformerTest.PreferenceFragmentWithSinglePreference;
import de.KnollFrank.settingssearch.test.TestActivity;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class SearchablePreferenceScreenGraphTestFactory {

    private SearchablePreferenceScreenGraphTestFactory() {
    }

    public static ImmutableValueGraph.Builder<SearchablePreferenceScreen, SearchablePreference> createGraphBuilder() {
        return Graphs.directedImmutableValueGraphBuilder();
    }

    public record Data(String DST_PREFERENCE_ID,
                       String PREFERENCE_CONNECTING_SRC_TO_DST_ID,
                       String PARENT_KEY,
                       String parentId,
                       String child1Id,
                       String child2Id,
                       String singleNodeScreenId,
                       String twoNodeScreen1Id,
                       String twoNodeScreen2Id) {
    }

    public record Trees(
            TreeAndDbDataProvider entityTreeAndDbDataProvider,
            Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> pojoTree) {
    }

    public static Trees createSingleNodeGraph(final FragmentClassOfActivity<? extends PreferenceFragmentCompat> host,
                                              final Locale locale,
                                              final Data data) {
        final String screenId = data.singleNodeScreenId();
        final SearchablePreferenceEntity preferenceConnectingSrcToDst =
                new SearchablePreferenceEntity(
                        data.PREFERENCE_CONNECTING_SRC_TO_DST_ID(),
                        "some key",
                        Optional.of("preference connected to TestPreferenceFragment"),
                        Optional.empty(),
                        Optional.empty(),
                        2131427444,
                        0,
                        Optional.of(TreeToPojoTreeTransformerTest.PreferenceFragmentWithSinglePreference.class.getName()),
                        Optional.empty(),
                        true,
                        LazyPersistableBundleFactory.fromBundle(new PersistableBundle()),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        screenId);
        final SearchablePreference preferencePojoConnectingSrcToDst =
                new SearchablePreference(
                        data.PREFERENCE_CONNECTING_SRC_TO_DST_ID(),
                        "some key",
                        Optional.of("preference connected to TestPreferenceFragment"),
                        Optional.empty(),
                        Optional.empty(),
                        2131427444,
                        0,
                        Optional.of(TreeToPojoTreeTransformerTest.PreferenceFragmentWithSinglePreference.class.getName()),
                        Optional.empty(),
                        true,
                        new PersistableBundle(),
                        Optional.empty(),
                        Set.of());
        final SearchablePreferenceScreenTreeEntity treeEntity =
                new SearchablePreferenceScreenTreeEntity(
                        locale,
                        PersistableBundleTestFactory.createSomePersistableBundle());
        final Pair<Pair<SearchablePreferenceScreenEntity, DbDataProviderData>, SearchablePreferenceScreen> src =
                createSrc(
                        screenId,
                        preferenceConnectingSrcToDst,
                        preferencePojoConnectingSrcToDst,
                        host,
                        treeEntity.id(),
                        data);
        return new Trees(
                new TreeAndDbDataProvider(
                        treeEntity,
                        DbDataProviderFactory.createDbDataProvider(
                                DbDataProviderDatas.merge(
                                        List.of(
                                                src.first().second(),
                                                DbDataProviderData
                                                        .builder()
                                                        .withNodesByGraph(
                                                                Map.of(
                                                                        treeEntity,
                                                                        Set.of(src.first().first())))
                                                        .withPredecessorByPreference(
                                                                Map.of(
                                                                        preferenceConnectingSrcToDst,
                                                                        Optional.empty()))
                                                        .withChildrenByPreference(
                                                                Map.of(
                                                                        preferenceConnectingSrcToDst,
                                                                        Set.of()))
                                                        .build())))),
                new Tree<>(
                        SearchablePreferenceScreenGraphTestFactory
                                .createGraphBuilder()
                                .addNode(src.second())
                                .build()));
    }

    public static Trees createGraph(final FragmentClassOfActivity<? extends PreferenceFragmentCompat> host,
                                    final Locale locale,
                                    final Data data) {
        final String screenId = data.twoNodeScreen1Id();
        final Class<? extends Fragment> fragment = PreferenceFragmentWithSinglePreference.class;
        final SearchablePreferenceEntity preferenceConnectingSrcToDst =
                new SearchablePreferenceEntity(
                        data.PREFERENCE_CONNECTING_SRC_TO_DST_ID(),
                        "some key",
                        Optional.of("preference connected to " + fragment.getSimpleName()),
                        Optional.empty(),
                        Optional.empty(),
                        2131427444,
                        0,
                        Optional.of(fragment.getName()),
                        Optional.empty(),
                        true,
                        LazyPersistableBundleFactory.fromBundle(new PersistableBundle()),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        screenId);
        final SearchablePreference preferencePojoConnectingSrcToDst =
                new SearchablePreference(
                        data.PREFERENCE_CONNECTING_SRC_TO_DST_ID(),
                        "some key",
                        Optional.of("preference connected to " + fragment.getSimpleName()),
                        Optional.empty(),
                        Optional.empty(),
                        2131427444,
                        0,
                        Optional.of(fragment.getName()),
                        Optional.empty(),
                        true,
                        new PersistableBundle(),
                        Optional.empty(),
                        Set.of());
        final SearchablePreferenceScreenTreeEntity treeEntity =
                new SearchablePreferenceScreenTreeEntity(
                        locale,
                        PersistableBundleTestFactory.createSomePersistableBundle());
        final Pair<Pair<SearchablePreferenceScreenEntity, DbDataProviderData>, SearchablePreferenceScreen> src =
                createSrc(
                        screenId,
                        preferenceConnectingSrcToDst,
                        preferencePojoConnectingSrcToDst,
                        host,
                        treeEntity.id(),
                        data);
        final Pair<Pair<SearchablePreferenceScreenEntity, DbDataProviderData>, SearchablePreferenceScreen> dst =
                createDst(
                        preferenceConnectingSrcToDst,
                        treeEntity.id(),
                        data);
        return new Trees(
                new TreeAndDbDataProvider(
                        treeEntity,
                        DbDataProviderFactory.createDbDataProvider(
                                DbDataProviderDatas.merge(
                                        List.of(
                                                src.first().second(),
                                                dst.first().second(),
                                                DbDataProviderData
                                                        .builder()
                                                        .withNodesByGraph(
                                                                Map.of(
                                                                        treeEntity,
                                                                        Set.of(
                                                                                src.first().first(),
                                                                                dst.first().first())))
                                                        .withPredecessorByPreference(
                                                                Map.of(
                                                                        preferenceConnectingSrcToDst,
                                                                        Optional.empty()))
                                                        .withChildrenByPreference(
                                                                Map.of(
                                                                        preferenceConnectingSrcToDst,
                                                                        Set.of()))
                                                        .build())))),
                new Tree<>(
                        SearchablePreferenceScreenGraphTestFactory
                                .createGraphBuilder()
                                .putEdgeValue(
                                        src.second(),
                                        dst.second(),
                                        preferencePojoConnectingSrcToDst)
                                .build()));
    }

    private static Pair<Pair<SearchablePreferenceScreenEntity, DbDataProviderData>, SearchablePreferenceScreen> createSrc(
            final String screenId,
            final SearchablePreferenceEntity preferenceConnectingSrcToDst,
            final SearchablePreference preferencePojoConnectingSrcToDst,
            final FragmentClassOfActivity<? extends PreferenceFragmentCompat> host,
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
                        LazyPersistableBundleFactory.fromBundle(new PersistableBundle()),
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
                        LazyPersistableBundleFactory.fromBundle(new PersistableBundle()),
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
                        new PersistableBundle(),
                        Optional.empty(),
                        Set.of());
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
                        LazyPersistableBundleFactory.fromBundle(new PersistableBundle()),
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
                        new PersistableBundle(),
                        Optional.empty(),
                        Set.of());
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
                        new PersistableBundle(),
                        Optional.empty(),
                        Set.of(child1Pojo, child2Pojo));
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
                                                Set.of(parent, child1, child2, preferenceConnectingSrcToDst)))
                                .withHostByPreference(
                                        ImmutableMap
                                                .<SearchablePreferenceEntity, SearchablePreferenceScreenEntity>builder()
                                                .put(parent, searchablePreferenceScreen)
                                                .put(child1, searchablePreferenceScreen)
                                                .put(child2, searchablePreferenceScreen)
                                                .put(preferenceConnectingSrcToDst, searchablePreferenceScreen)
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
                        Set.of(parentPojo, child1Pojo, child2Pojo, preferencePojoConnectingSrcToDst)));
    }

    private static Pair<Pair<SearchablePreferenceScreenEntity, DbDataProviderData>, SearchablePreferenceScreen> createDst(
            final SearchablePreferenceEntity predecessor,
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
                        LazyPersistableBundleFactory.fromBundle(new PersistableBundle()),
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
                        new PersistableBundle(),
                        Optional.empty(),
                        Set.of());
        final FragmentClassOfActivity<PreferenceFragmentWithSinglePreference> host =
                new FragmentClassOfActivity<>(
                        PreferenceFragmentWithSinglePreference.class,
                        new ActivityDescription(
                                TestActivity.class,
                                new PersistableBundle()));
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
                        host,
                        Optional.of("screen title"),
                        Optional.of("screen summary"),
                        Set.of(searchablePreferencePojo)));
    }
}
