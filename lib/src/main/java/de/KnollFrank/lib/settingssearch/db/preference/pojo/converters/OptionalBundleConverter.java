package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import android.os.Bundle;

import androidx.room.TypeConverter;

import java.util.Optional;

public class OptionalBundleConverter implements Converter<Optional<Bundle>, String> {

    private static final OptionalConverter<Bundle> converter = new OptionalConverter<>(new BundleConverter());

    @TypeConverter
    @Override
    public String doForward(final Optional<Bundle> value) {
        return converter.doForward(value);
    }

    @TypeConverter
    @Override
    public Optional<Bundle> doBackward(final String string) {
        return converter.doBackward(string);
    }
}
