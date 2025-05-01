package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import android.os.Bundle;

import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import de.KnollFrank.lib.settingssearch.db.preference.dao.JsonDAO;

// FK-TODO: remove de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.Converter and use com.google.common.base.Converter instead
class Bundle2BytesConverter {

    public byte[] bundle2Bytes(final Bundle bundle) {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonDAO.persist(bundle, output);
        return output.toByteArray();
    }

    public Bundle bytes2Bundle(final byte[] bytes) {
        return JsonDAO.load(
                new ByteArrayInputStream(bytes),
                new TypeToken<>() {
                });
    }
}
