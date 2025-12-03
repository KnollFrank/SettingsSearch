package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import android.annotation.SuppressLint;
import android.os.PersistableBundle;

import androidx.room.TypeConverter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PersistableBundleConverter implements Converter<PersistableBundle, String> {

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    @Override
    @TypeConverter
    @SuppressLint("NewApi")
    public String convertForward(final PersistableBundle bundle) {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            bundle.writeToStream(outputStream);
            return outputStream.toString(CHARSET);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @TypeConverter
    @SuppressLint("NewApi")
    public PersistableBundle convertBackward(final String xmlString) {
        try (final ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlString.getBytes(CHARSET))) {
            return PersistableBundle.readFromStream(inputStream);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
