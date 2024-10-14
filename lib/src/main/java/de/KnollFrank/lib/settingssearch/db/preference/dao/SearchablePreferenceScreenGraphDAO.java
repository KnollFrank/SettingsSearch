package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.preference.PreferenceManager;

import org.jgrapht.Graph;

import java.io.InputStream;
import java.io.OutputStream;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.graph.Graph2POJOGraphTransformer;
import de.KnollFrank.lib.settingssearch.graph.POJOGraph2GraphTransformer;

public class SearchablePreferenceScreenGraphDAO {

    public static void persist(final Graph<PreferenceScreenWithHostClass, PreferenceEdge> source, final OutputStream sink) {
        final var pojoGraph = Graph2POJOGraphTransformer.transformGraph2POJOGraph(source);
        POJOGraphDAO.persist(pojoGraph, sink);
    }

    public static Graph<PreferenceScreenWithHostClass, PreferenceEdge> load(final InputStream source,
                                                                            final PreferenceManager preferenceManager) {
        final var pojoGraph = POJOGraphDAO.load(source);
        return POJOGraph2GraphTransformer.transformPOJOGraph2Graph(pojoGraph, preferenceManager);
    }
}
