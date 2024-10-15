package de.KnollFrank.lib.settingssearch.common.converter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

class StringAndBytesConverter {

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public static byte[] string2Bytes(final String string) {
        return string.getBytes(CHARSET);
    }

    public static String bytes2String(final byte[] bytes) {
        return new String(bytes, CHARSET);
    }
}
