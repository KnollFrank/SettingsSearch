package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import android.os.Bundle;

import androidx.room.TypeConverter;

public class BundleConverter implements Converter<Bundle> {

    private final Converter<byte[]> byteArrayConverter = new ByteArrayConverter();
    private final Bundle2BytesConverter bundle2BytesConverter = new Bundle2BytesConverter();

    @TypeConverter
    @Override
    public String toString(final Bundle bundle) {
        return byteArrayConverter.toString(bundle2BytesConverter.bundle2Bytes(bundle));
    }

    @TypeConverter
    @Override
    public Bundle fromString(final String string) {
        return bundle2BytesConverter.bytes2Bundle(byteArrayConverter.fromString(string));
    }
}
