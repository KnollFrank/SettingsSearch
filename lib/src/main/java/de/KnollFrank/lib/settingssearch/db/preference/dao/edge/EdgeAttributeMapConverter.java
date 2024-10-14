package de.KnollFrank.lib.settingssearch.db.preference.dao.edge;

import com.google.gson.reflect.TypeToken;

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.common.IOUtils;
import de.KnollFrank.lib.settingssearch.db.preference.dao.JsonDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

public class EdgeAttributeMapConverter {

    public static SearchablePreferencePOJOEdge attributeMap2Edge(final Map<String, Attribute> attributeMap) {
        return new SearchablePreferencePOJOEdge(
                json2SearchablePreferencePOJO(
                        attributeMap.get("searchablePreference").getValue()));
    }

    public static Map<String, Attribute> edge2AttributeMap(final SearchablePreferencePOJOEdge edge) {
        return Map.of(
                "searchablePreference",
                DefaultAttribute.createAttribute(convert2JSON(edge.preference)));
    }

    private static SearchablePreferencePOJO json2SearchablePreferencePOJO(final String json) {
        return JsonDAO.load(
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)),
                new TypeToken<>() {
                });
    }

    private static String convert2JSON(final SearchablePreferencePOJO preference) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonDAO.persist(preference, outputStream);
        return IOUtils.toString(outputStream);
    }
}
