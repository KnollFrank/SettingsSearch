package de.KnollFrank.lib.settingssearch.common.converter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

class StringAndBytesConverter {

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private StringAndBytesConverter() {
    }

    public static byte[] stringToBytes(final String string) {
        return string.getBytes(CHARSET);
    }

    public static String bytesToString(final byte[] bytes) {
        return new String(bytes, CHARSET);
    }
}
