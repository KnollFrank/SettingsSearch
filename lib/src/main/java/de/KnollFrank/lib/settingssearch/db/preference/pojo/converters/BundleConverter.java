package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import android.os.Bundle;

import androidx.room.TypeConverter;

public class BundleConverter implements Converter<Bundle, String> {

    private final Converter<byte[], String> byteArrayConverter = new ByteArrayConverter();
    private final Converter<Bundle, byte[]> bundle2BytesConverter = new Bundle2ByteArrayConverter();

    @TypeConverter
    @Override
    public String doForward(final Bundle bundle) {
        return byteArrayConverter.doForward(bundle2BytesConverter.doForward(bundle));
    }

    @TypeConverter
    @Override
    public Bundle doBackward(final String string) {
        return bundle2BytesConverter.doBackward(byteArrayConverter.doBackward(string));
    }
}
