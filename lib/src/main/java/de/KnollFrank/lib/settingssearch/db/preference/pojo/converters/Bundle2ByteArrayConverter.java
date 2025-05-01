package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import android.os.Bundle;

import androidx.room.TypeConverter;

import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import de.KnollFrank.lib.settingssearch.db.preference.dao.JsonDAO;

class Bundle2ByteArrayConverter implements Converter<Bundle, byte[]> {

    @TypeConverter
    @Override
    public byte[] doForward(final Bundle bundle) {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonDAO.persist(bundle, output);
        return output.toByteArray();
    }

    @TypeConverter
    @Override
    public Bundle doBackward(final byte[] bytes) {
        return JsonDAO.load(
                new ByteArrayInputStream(bytes),
                new TypeToken<>() {
                });
    }
}
