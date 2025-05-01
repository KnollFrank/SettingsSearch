package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import java.util.Optional;

public class OptionalStringConverter implements Converter<Optional<String>> {

    private static final OptionalConverter<String> converter =
            new OptionalConverter<>(
                    new Converter<>() {

                        @Override
                        public String toString(final String value) {
                            return value;
                        }

                        @Override
                        public String fromString(final String string) {
                            return string;
                        }
                    });

    @TypeConverter
    @Override
    public String toString(final Optional<String> value) {
        return converter.toString(value);
    }

    @TypeConverter
    @Override
    public Optional<String> fromString(final String string) {
        return converter.fromString(string);
    }
}
