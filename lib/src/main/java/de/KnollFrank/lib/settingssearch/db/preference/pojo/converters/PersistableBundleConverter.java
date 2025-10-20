package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import android.annotation.SuppressLint;
import android.os.PersistableBundle;

import androidx.room.TypeConverter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class PersistableBundleConverter implements Converter<PersistableBundle, String> {

    @Override
    @TypeConverter
    @SuppressLint("NewApi")
    public String doForward(final PersistableBundle bundle) {
        return Optional
                .ofNullable(bundle)
                .map(
                        _bundle -> {
                            try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                                _bundle.writeToStream(outputStream);
                                return outputStream.toString(StandardCharsets.UTF_8);
                            } catch (final IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                .orElse(null);
    }

    @Override
    @TypeConverter
    @SuppressLint("NewApi")
    public PersistableBundle doBackward(final String xmlString) {
        return Optional
                .ofNullable(xmlString)
                .map(
                        _xmlString -> {
                            try (final ByteArrayInputStream inputStream = new ByteArrayInputStream(_xmlString.getBytes(StandardCharsets.UTF_8))) {
                                return PersistableBundle.readFromStream(inputStream);
                            } catch (final IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                .orElse(null);
    }
}
