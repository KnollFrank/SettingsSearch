package de.KnollFrank.lib.settingssearch.db.preference.dao;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.nio.json.JSONImporter;

import java.io.Reader;

import de.KnollFrank.lib.settingssearch.db.preference.dao.converter.EdgeAttributeMapConverter;
import de.KnollFrank.lib.settingssearch.db.preference.dao.converter.VertexAttributeMapConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

// FK-TODO: remove?
class JSON2POJOGraphConverter {

    public static Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> json2PojoGraph(final Reader reader) {
        return importGraph(getJSONImporter(), reader);
    }

    private static JSONImporter<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> getJSONImporter() {
        final JSONImporter<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> importer = new JSONImporter<>();
        importer.setVertexWithAttributesFactory((vertexIdentifier, attrs) -> VertexAttributeMapConverter.attributeMap2Vertex(attrs));
        importer.setEdgeWithAttributesFactory(EdgeAttributeMapConverter::attributeMap2Edge);
        return importer;
    }

    private static Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> importGraph(
            final JSONImporter<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> importer,
            final Reader reader) {
        final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> graph = new DefaultDirectedGraph<>(SearchablePreferencePOJOEdge.class);
        importer.importGraph(graph, reader);
        return graph;
    }
}
