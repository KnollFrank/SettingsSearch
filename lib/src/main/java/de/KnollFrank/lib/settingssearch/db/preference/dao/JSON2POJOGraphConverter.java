package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.gson.reflect.TypeToken;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.json.JSONImporter;

import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.db.preference.dao.vertex.VertexAttributeMapConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

class JSON2POJOGraphConverter {

    public static Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> json2PojoGraph(final Reader reader) {
        return importGraph(getJSONImporter(), reader);
    }

    private static JSONImporter<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> getJSONImporter() {
        final JSONImporter<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> importer = new JSONImporter<>();
        importer.setVertexWithAttributesFactory((vertexIdentifier, attrs) -> VertexAttributeMapConverter.attributeMap2Vertex(attrs));
        importer.setEdgeWithAttributesFactory(JSON2POJOGraphConverter::getEdgeWithAttributes);
        return importer;
    }

    private static SearchablePreferencePOJOEdge getEdgeWithAttributes(final Map<String, Attribute> attrs) {
        return new SearchablePreferencePOJOEdge(
                json2SearchablePreferencePOJO(
                        attrs.get("searchablePreference").getValue()));
    }

    private static SearchablePreferencePOJO json2SearchablePreferencePOJO(final String json) {
        return JsonDAO.load(
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)),
                new TypeToken<>() {
                });
    }

    private static Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> importGraph(
            final JSONImporter<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> importer,
            final Reader reader) {
        final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> graph = new DefaultDirectedGraph<>(SearchablePreferencePOJOEdge.class);
        importer.importGraph(graph, reader);
        return graph;
    }
}
