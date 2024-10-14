package de.KnollFrank.lib.settingssearch.db.preference.dao.vertex;

import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import de.KnollFrank.lib.settingssearch.common.IOUtils;
import de.KnollFrank.lib.settingssearch.db.preference.dao.JsonDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;

class VertexConverter {

    public static String vertex2JSON(final PreferenceScreenWithHostClassPOJO vertex) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonDAO.persist(vertex, outputStream);
        return IOUtils.toString(outputStream);
    }

    public static PreferenceScreenWithHostClassPOJO json2Vertex(final String json) {
        return JsonDAO.load(
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)),
                new TypeToken<>() {
                });
    }
}
