package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.Locale;

public class LocaleConverter implements Converter<Locale, java.util.Locale> {

    @Override
    public java.util.Locale doForward(final Locale locale) {
        return new java.util.Locale(locale.language());
    }

    @Override
    public Locale doBackward(final java.util.Locale locale) {
        return new Locale(locale.getLanguage());
    }

    public Set<java.util.Locale> doForward(final Set<Locale> locales) {
        return locales
                .stream()
                .map(this::doForward)
                .collect(Collectors.toSet());
    }
}
