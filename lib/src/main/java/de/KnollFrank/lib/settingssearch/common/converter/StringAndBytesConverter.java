package de.KnollFrank.lib.settingssearch.common.converter;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

class StringAndBytesConverter {

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public static byte[] string2Bytes(final String string) {
        return Base64.encodeBase64(string.getBytes(CHARSET));
    }

    public static String bytes2String(final byte[] bytes) {
        return new String(Base64.decodeBase64(bytes), CHARSET);
    }
}
