package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.HashMap;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.common.BreadthFirstGraphVisitor;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;

public class PreferenceScreensProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final IsPreferenceSearchable isPreferenceSearchable;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider;

    public PreferenceScreensProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                     final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                     final IsPreferenceSearchable isPreferenceSearchable,
                                     final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                     final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.searchableInfoAndDialogInfoProvider = searchableInfoAndDialogInfoProvider;
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
        final BreadthFirstGraphVisitor<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraphVisitor =
                new BreadthFirstGraphVisitor<>() {

                    private final Map<PreferenceScreenWithHost, SearchablePreferenceScreenWithHost> newNodeByOldNode = new HashMap<>();

                    @Override
                    protected void visitRootNode(final PreferenceScreenWithHost rootNode) {
                        final SearchablePreferenceScreenWithHost searchablePreferenceScreenWithHost =
                                PreferenceScreenWithHostFactory.createSearchablePreferenceScreenWithHost(
                                        rootNode.host(),
                                        isPreferenceSearchable,
                                        searchableInfoAndDialogInfoProvider);
                        newNodeByOldNode.put(rootNode, searchablePreferenceScreenWithHost);
                        transformedGraph.addVertex(searchablePreferenceScreenWithHost);
                    }

                    @Override
                    protected void visitInnerNode(final PreferenceScreenWithHost node, final PreferenceScreenWithHost parentNode) {
                        final SearchablePreferenceScreenWithHost searchableNode =
                                PreferenceScreenWithHostFactory.createSearchablePreferenceScreenWithHost(
                                        node.host(),
                                        isPreferenceSearchable,
                                        searchableInfoAndDialogInfoProvider);
                        newNodeByOldNode.put(node, searchableNode);
                        transformedGraph.addVertex(searchableNode);
                        transformedGraph.addEdge(
                                newNodeByOldNode.get(parentNode),
                                newNodeByOldNode.get(node),
                                new PreferenceEdge(
                                        newNodeByOldNode
                                                .get(parentNode)
                                                .searchablePreferenceScreen()
                                                .searchablePreferenceByPreference()
                                                .get(preferenceScreenGraph.getEdge(parentNode, node).preference)));
                    }
                };
        preferenceScreenGraphVisitor.visit(preferenceScreenGraph);
        return transformedGraph;
    }
}
