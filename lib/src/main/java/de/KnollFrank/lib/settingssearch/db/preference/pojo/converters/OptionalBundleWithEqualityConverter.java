package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.BundleWithEquality;

// FK-TODO: unit test
public class OptionalBundleWithEqualityConverter implements Converter<Optional<BundleWithEquality>, String> {

    private static final OptionalConverter<BundleWithEquality> converter = new OptionalConverter<>(new BundleWithEqualityConverter());

    @TypeConverter
    @Override
    public String doForward(final Optional<BundleWithEquality> value) {
        return converter.doForward(value);
    }

    @TypeConverter
    @Override
    public Optional<BundleWithEquality> doBackward(final String string) {
        return converter.doBackward(string);
    }
}
