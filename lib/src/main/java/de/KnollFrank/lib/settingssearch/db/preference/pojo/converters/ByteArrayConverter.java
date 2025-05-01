package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

class ByteArrayConverter implements Converter<byte[], String> {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    @TypeConverter
    @Override
    public String doForward(final byte[] byteArray) {
        return new String(byteArray, UTF_8);
    }

    @TypeConverter
    @Override
    public byte[] doBackward(final String string) {
        return string.getBytes(UTF_8);
    }
}
