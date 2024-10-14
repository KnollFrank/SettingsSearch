package de.KnollFrank.lib.settingssearch.db.preference.dao.vertex;

import static de.KnollFrank.lib.settingssearch.db.preference.dao.vertex.AttributeMapConverter.attribute2Map;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.vertex.AttributeMapConverter.map2Attribute;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.vertex.VertexAttributeConverter.attribute2Vertex;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.vertex.VertexAttributeConverter.vertex2Attribute;

import org.jgrapht.nio.Attribute;

import java.util.Map;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;

public class VertexAttributeMapConverter {

    public static Map<String, Attribute> vertex2AttributeMap(final PreferenceScreenWithHostClassPOJO vertex) {
        return attribute2Map(vertex2Attribute(vertex));
    }

    public static PreferenceScreenWithHostClassPOJO attributeMap2Vertex(final Map<String, Attribute> attributes) {
        return attribute2Vertex(map2Attribute(attributes));
    }
}
