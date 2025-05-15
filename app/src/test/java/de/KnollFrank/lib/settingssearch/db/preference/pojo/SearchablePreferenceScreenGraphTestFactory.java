package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.Bundle;

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

    public static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> createPojoGraph(final Class<? extends PreferenceFragmentCompat> host) {
        final String screenId = "1";
        final SearchablePreference preferenceConnectingSrc2Dst =
                new SearchablePreference(
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
                        new Bundle(),
                        Optional.empty(),
                        Optional.empty(),
                        screenId);
        return DefaultDirectedGraph
                .<SearchablePreferenceScreen, SearchablePreferenceEdge>createBuilder(SearchablePreferenceEdge.class)
                .addEdge(
                        createSrc(screenId, preferenceConnectingSrc2Dst, host),
                        createDst(Optional.of(screenId), preferenceConnectingSrc2Dst),
                        new SearchablePreferenceEdge(preferenceConnectingSrc2Dst))
                .build();
    }

    private static SearchablePreferenceScreen createSrc(final String screenId,
                                                        final SearchablePreference preferenceConnectingSrc2Dst,
                                                        final Class<? extends PreferenceFragmentCompat> host) {
        final SearchablePreference parent =
                new SearchablePreference(
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
                        new Bundle(),
                        Optional.empty(),
                        Optional.empty(),
                        screenId);
        return new SearchablePreferenceScreen(
                screenId,
                new HostWithArguments(host, Optional.empty()),
                Optional.of("screen title"),
                Optional.of("screen summary"),
                Set.of(
                        parent,
                        new SearchablePreference(
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
                                new Bundle(),
                                Optional.of(1),
                                Optional.empty(),
                                screenId),
                        new SearchablePreference(
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
                                new Bundle(),
                                Optional.of(1),
                                Optional.empty(),
                                screenId),
                        preferenceConnectingSrc2Dst),
                Optional.empty());
    }

    private static SearchablePreferenceScreen createDst(final Optional<String> parentScreenId,
                                                        final SearchablePreference predecessor) {
        final String screenId = "2";
        final SearchablePreference e1 =
                new SearchablePreference(
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
                        new Bundle(),
                        Optional.empty(),
                        Optional.of(predecessor.getId()),
                        screenId);
        return new SearchablePreferenceScreen(
                screenId,
                new HostWithArguments(Graph2POJOGraphTransformerTest.PreferenceFragmentWithSinglePreference.class, Optional.empty()),
                Optional.empty(),
                Optional.empty(),
                Set.of(e1),
                parentScreenId);
    }
}
