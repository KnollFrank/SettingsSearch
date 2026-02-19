package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.LanguageCode;

public class LanguageCodeConverter implements Converter<LanguageCode, String> {

    @TypeConverter
    @Override
    public String convertForward(final LanguageCode languageCode) {
        return languageCode.code();
    }

    @TypeConverter
    @Override
    public LanguageCode convertBackward(final String code) {
        return new LanguageCode(code);
    }

    public Set<LanguageCode> convertBackward(final Set<String> codes) {
        return codes
                .stream()
                .map(this::convertBackward)
                .collect(Collectors.toSet());
    }
}
