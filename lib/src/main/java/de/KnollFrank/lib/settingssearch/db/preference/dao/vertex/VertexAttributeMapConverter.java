package de.KnollFrank.lib.settingssearch.db.preference.dao.vertex;

import static de.KnollFrank.lib.settingssearch.db.preference.dao.vertex.VertexAttributeConverter.attribute2Vertex;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.vertex.VertexAttributeConverter.vertex2Attribute;

import org.jgrapht.nio.Attribute;

import java.util.Map;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;

public class VertexAttributeMapConverter {

    private static final AttributeMapConverter attributeMapConverter = new AttributeMapConverter("preferenceScreenWithHostClass");

    public static Map<String, Attribute> vertex2AttributeMap(final PreferenceScreenWithHostClassPOJO vertex) {
        return attributeMapConverter.attribute2AttributeMap(vertex2Attribute(vertex));
    }

    public static PreferenceScreenWithHostClassPOJO attributeMap2Vertex(final Map<String, Attribute> attributeMap) {
        return attribute2Vertex(attributeMapConverter.attributeMap2Attribute(attributeMap));
    }
}
