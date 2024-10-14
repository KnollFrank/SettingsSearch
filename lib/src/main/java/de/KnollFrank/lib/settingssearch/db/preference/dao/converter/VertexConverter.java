package de.KnollFrank.lib.settingssearch.db.preference.dao.converter;

import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import de.KnollFrank.lib.settingssearch.common.IOUtils;
import de.KnollFrank.lib.settingssearch.db.preference.dao.JsonDAO;

class VertexConverter {

    public static <T> String entity2JSON(final T entity) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonDAO.persist(entity, outputStream);
        return IOUtils.toString(outputStream);
    }

    public static <T> T json2Entity(final String json, final Class<T> clazzOfEntity) {
        return JsonDAO.load(
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)),
                TypeToken.get(clazzOfEntity));
    }
}
