package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

class ByteArrayConverter implements Converter<byte[]> {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    @Override
    public String toString(final byte[] byteArray) {
        return new String(byteArray, UTF_8);
    }

    @Override
    public byte[] fromString(final String string) {
        return string.getBytes(UTF_8);
    }
}
