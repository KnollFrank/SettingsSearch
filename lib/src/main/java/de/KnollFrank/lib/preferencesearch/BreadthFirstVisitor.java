package de.KnollFrank.lib.preferencesearch;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

// FK-TODO: generalize
abstract class BreadthFirstVisitor {

    public void visit(final Graph<PreferenceScreenWithHost, PreferenceEdge> graph) {
        final BreadthFirstIterator<PreferenceScreenWithHost, PreferenceEdge> iterator = new BreadthFirstIterator<>(graph);
        while (iterator.hasNext()) {
            final PreferenceScreenWithHost preferenceScreen = iterator.next();
            final PreferenceScreenWithHost parentPreferenceScreen = iterator.getParent(preferenceScreen);
            if (parentPreferenceScreen == null) {
                visitRootNode(preferenceScreen);
            } else {
                visitInnerNode(preferenceScreen, parentPreferenceScreen);
            }
        }
    }

    protected abstract void visitRootNode(final PreferenceScreenWithHost rootNode);

    protected abstract void visitInnerNode(final PreferenceScreenWithHost preferenceScreen, final PreferenceScreenWithHost parentPreferenceScreen);
}
