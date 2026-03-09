package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import android.os.LocaleList;

import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

public class LocaleListConverter implements Converter<LocaleList, List<Locale>> {

    @Override
    public List<Locale> convertForward(final LocaleList localeList) {
        return IntStream
                .range(0, localeList.size())
                .mapToObj(localeList::get)
                .toList();
    }

    @Override
    public LocaleList convertBackward(final List<Locale> locales) {
        return new LocaleList(locales.toArray(new Locale[0]));
    }
}
