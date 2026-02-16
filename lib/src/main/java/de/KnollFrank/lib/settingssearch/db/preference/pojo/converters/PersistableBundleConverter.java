package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import android.annotation.SuppressLint;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PersistableBundleConverter implements Converter<PersistableBundle, String> {

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    @TypeConverter
    @Override
    @SuppressLint("NewApi")
    @Nullable
    public String convertForward(final PersistableBundle bundle) {
        if (bundle.isEmpty()) {
            return null;
        }
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            bundle.writeToStream(outputStream);
            return outputStream.toString(CHARSET);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TypeConverter
    @Override
    @SuppressLint("NewApi")
    public PersistableBundle convertBackward(@Nullable final String xmlString) {
        if (xmlString == null) {
            return new PersistableBundle();
        }
        try (final ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlString.getBytes(CHARSET))) {
            return PersistableBundle.readFromStream(inputStream);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
