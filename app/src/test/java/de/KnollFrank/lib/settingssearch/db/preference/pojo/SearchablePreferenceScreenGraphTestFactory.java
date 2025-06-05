package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.graph.Graph2POJOGraphTransformerTest;

public class SearchablePreferenceScreenGraphTestFactory {

    public static final int DST_PREFERENCE_ID = 5;
    public static final int PREFERENCE_CONNECTING_SRC_2_DST_ID = 4;
    public static final String PARENT_KEY = "parentKey";

    public static Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> createSingleNodePojoGraph(final Class<? extends PreferenceFragmentCompat> host) {
        final String screenId = "1";
        final SearchablePreferenceEntity preferenceConnectingSrc2Dst =
                new SearchablePreferenceEntity(
                        PREFERENCE_CONNECTING_SRC_2_DST_ID,
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
        return DefaultDirectedGraph
                .<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge>createBuilder(SearchablePreferenceEntityEdge.class)
                .addVertex(createSrc(screenId, preferenceConnectingSrc2Dst, host))
                .build();
    }

    public static Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> createPojoGraph(final Class<? extends PreferenceFragmentCompat> host) {
        final String screenId = "1";
        final SearchablePreferenceEntity preferenceConnectingSrc2Dst =
                new SearchablePreferenceEntity(
                        PREFERENCE_CONNECTING_SRC_2_DST_ID,
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
        return DefaultDirectedGraph
                .<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge>createBuilder(SearchablePreferenceEntityEdge.class)
                .addEdge(
                        createSrc(screenId, preferenceConnectingSrc2Dst, host),
                        createDst(preferenceConnectingSrc2Dst),
                        new SearchablePreferenceEntityEdge(preferenceConnectingSrc2Dst))
                .build();
    }

    private static SearchablePreferenceScreenEntity createSrc(final String screenId,
                                                              final SearchablePreferenceEntity preferenceConnectingSrc2Dst,
                                                              final Class<? extends PreferenceFragmentCompat> host) {
        final SearchablePreferenceEntity parent =
                new SearchablePreferenceEntity(
                        1,
                        PARENT_KEY,
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
        return new SearchablePreferenceScreenEntity(
                screenId,
                host,
                Optional.of("screen title"),
                Optional.of("screen summary"),
                Set.of(
                        parent,
                        new SearchablePreferenceEntity(
                                2,
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
                                Optional.of(parent.getId()),
                                Optional.empty(),
                                screenId),
                        new SearchablePreferenceEntity(
                                3,
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
                                Optional.of(parent.getId()),
                                Optional.empty(),
                                screenId),
                        preferenceConnectingSrc2Dst));
    }

    private static SearchablePreferenceScreenEntity createDst(final SearchablePreferenceEntity predecessor) {
        final String screenId = "2";
        final SearchablePreferenceEntity e1 =
                new SearchablePreferenceEntity(
                        DST_PREFERENCE_ID,
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
                        Optional.of(predecessor.getId()),
                        screenId);
        return new SearchablePreferenceScreenEntity(
                screenId,
                Graph2POJOGraphTransformerTest.PreferenceFragmentWithSinglePreference.class,
                Optional.empty(),
                Optional.empty(),
                Set.of(e1));
    }
}
