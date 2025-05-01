package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import java.util.Optional;

public class OptionalStringConverter implements Converter<Optional<String>, String> {

    private static final Converter<Optional<String>, String> converter =
            new OptionalConverter<>(
                    new Converter<>() {

                        @Override
                        public String doForward(final String string) {
                            return string;
                        }

                        @Override
                        public String doBackward(final String string) {
                            return string;
                        }
                    });

    @TypeConverter
    @Override
    public String doForward(final Optional<String> value) {
        return converter.doForward(value);
    }

    @TypeConverter
    @Override
    public Optional<String> doBackward(final String string) {
        return converter.doBackward(string);
    }
}
