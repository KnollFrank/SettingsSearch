package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import java.util.Optional;

public class OptionalStringConverter implements Converter<Optional<String>, String> {

    private static final Converter<Optional<String>, String> converter =
            new OptionalConverter<>(
                    new Converter<>() {

                        @Override
                        public String convertForward(final String string) {
                            return string;
                        }

                        @Override
                        public String convertBackward(final String string) {
                            return string;
                        }
                    });

    @TypeConverter
    @Override
    public String convertForward(final Optional<String> value) {
        return converter.convertForward(value);
    }

    @TypeConverter
    @Override
    public Optional<String> convertBackward(final String string) {
        return converter.convertBackward(string);
    }
}
