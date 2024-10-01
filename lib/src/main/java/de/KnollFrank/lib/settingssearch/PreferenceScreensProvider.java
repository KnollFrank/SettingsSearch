package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.HashMap;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.common.BreadthFirstGraphVisitor;
import de.KnollFrank.lib.settingssearch.provider.ISearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class PreferenceScreensProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final SearchableInfoProvider searchableInfoProvider;
    private final ISearchableDialogInfoOfProvider searchableDialogInfoOfProvider;
    private final IsPreferenceSearchable isPreferenceSearchable;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;

    public PreferenceScreensProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                     final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                     final SearchableInfoProvider searchableInfoProvider,
                                     final ISearchableDialogInfoOfProvider searchableDialogInfoOfProvider,
                                     final IsPreferenceSearchable isPreferenceSearchable,
                                     final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.searchableInfoProvider = searchableInfoProvider;
        this.searchableDialogInfoOfProvider = searchableDialogInfoOfProvider;
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
    }

    public ConnectedPreferenceScreens getConnectedPreferenceScreens(final PreferenceFragmentCompat root) {
        return new ConnectedPreferenceScreens(getPreferenceScreenGraph(root));
    }

    private Graph<SearchablePreferenceScreenWithHost, PreferenceEdge> getPreferenceScreenGraph(final PreferenceFragmentCompat root) {
        final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph =
                new PreferenceScreenGraphProvider(preferenceScreenWithHostProvider, preferenceConnected2PreferenceFragmentProvider)
                        .getPreferenceScreenGraph(
                                PreferenceScreenWithHostFactory.createPreferenceScreenWithHost(
                                        root));
        preferenceScreenGraphAvailableListener.onPreferenceScreenGraphWithoutInvisibleAndNonSearchablePreferencesAvailable(preferenceScreenGraph);
        return transformPreferences2SearchablePreferences(preferenceScreenGraph);
    }

    // FK-TODO: refactor to GraphTransformer
    private Graph<SearchablePreferenceScreenWithHost, PreferenceEdge> transformPreferences2SearchablePreferences(final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        final DefaultDirectedGraph<SearchablePreferenceScreenWithHost, PreferenceEdge> transformedGraph = new DefaultDirectedGraph<>(PreferenceEdge.class);
        final Map<PreferenceScreenWithHost, SearchablePreferenceScreenWithHost> newNodeByOldNode = new HashMap<>();
        final BreadthFirstGraphVisitor<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraphVisitor =
                new BreadthFirstGraphVisitor<>() {

                    @Override
                    protected void visitRootNode(final PreferenceScreenWithHost rootNode) {
                        final SearchablePreferenceScreenWithHost searchablePreferenceScreenWithHost =
                                PreferenceScreenWithHostFactory.createSearchablePreferenceScreenWithHost(
                                        rootNode.host(),
                                        searchableInfoProvider,
                                        searchableDialogInfoOfProvider,
                                        isPreferenceSearchable);
                        newNodeByOldNode.put(rootNode, searchablePreferenceScreenWithHost);
                        transformedGraph.addVertex(searchablePreferenceScreenWithHost);
                    }

                    @Override
                    protected void visitInnerNode(final PreferenceScreenWithHost node, final PreferenceScreenWithHost parentNode) {
                        final SearchablePreferenceScreenWithHost searchableNode =
                                PreferenceScreenWithHostFactory.createSearchablePreferenceScreenWithHost(
                                        node.host(),
                                        searchableInfoProvider,
                                        searchableDialogInfoOfProvider,
                                        isPreferenceSearchable);
                        newNodeByOldNode.put(node, searchableNode);
                        transformedGraph.addVertex(searchableNode);
                        transformedGraph.addEdge(
                                newNodeByOldNode.get(parentNode),
                                newNodeByOldNode.get(node),
                                new PreferenceEdge(
                                        newNodeByOldNode
                                                .get(parentNode)
                                                .searchablePreferenceScreen()
                                                .newPreferenceByOldPreference()
                                                .get(preferenceScreenGraph.getEdge(parentNode, node).preference)));
                    }
                };
        preferenceScreenGraphVisitor.visit(preferenceScreenGraph);
        return transformedGraph;
    }
}
