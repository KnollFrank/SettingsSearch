package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.gson.reflect.TypeToken;

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.common.IOUtils;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;

class VertexAttribute {

    private static final String PREFERENCE_SCREEN_WITH_HOST_CLASS = "preferenceScreenWithHostClass";

    public static Map<String, Attribute> vertex2Attributes(final PreferenceScreenWithHostClassPOJO preferenceScreenWithHostClass) {
        return Map.of(
                PREFERENCE_SCREEN_WITH_HOST_CLASS,
                DefaultAttribute.createAttribute(convert2JSON(preferenceScreenWithHostClass)));
    }

    public static PreferenceScreenWithHostClassPOJO attributes2Vertex(final Map<String, Attribute> attrs) {
        return json2PreferenceScreenWithHostClassPOJO(
                attrs.get(PREFERENCE_SCREEN_WITH_HOST_CLASS).getValue());
    }

    private static String convert2JSON(final PreferenceScreenWithHostClassPOJO preferenceScreenWithHostClass) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonDAO.persist(preferenceScreenWithHostClass, outputStream);
        return IOUtils.toString(outputStream);
    }

    private static PreferenceScreenWithHostClassPOJO json2PreferenceScreenWithHostClassPOJO(final String json) {
        return JsonDAO.load(
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)),
                new TypeToken<>() {
                });
    }
}
