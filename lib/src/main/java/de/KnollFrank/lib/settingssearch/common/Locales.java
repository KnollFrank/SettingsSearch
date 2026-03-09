package de.KnollFrank.lib.settingssearch.common;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.LocaleList;

import androidx.annotation.Size;
import androidx.annotation.XmlRes;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class Locales {

    private Locales() {
    }

    public static Locale getCurrentLocaleOrDefault(final LocaleList localeList) {
        return getCurrentLocale(localeList.isEmpty() ? LocaleList.getAdjustedDefault() : localeList);
    }

    public static Locale getActualUsedLocale(final LocaleList systemLocales, final List<Locale> appLocales) {
        return Locales
                .getFirstMatch(systemLocales, appLocales)
                .or(() -> appLocales.stream().findFirst())
                .orElseGet(() -> getCurrentLocaleOrDefault(systemLocales));
    }

    // FK-TODO: unit test and refactor
    public static List<Locale> readLocales(final Resources resources, final @XmlRes int resId) {
        if (resId == 0) {
            return new ArrayList<>();
        }
        final List<Locale> availableLocales = new ArrayList<>();
        try (final XmlResourceParser xrp = resources.getXml(resId)) {
            for (int eventType = xrp.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = xrp.next()) {
                if (eventType == XmlPullParser.START_TAG && "locale".equals(xrp.getName())) {
                    final String localeTag = xrp.getAttributeValue("http://schemas.android.com/apk/res/android", "name");
                    if (localeTag != null) {
                        availableLocales.add(Locale.forLanguageTag(localeTag));
                    }
                }
            }
        } catch (final XmlPullParserException | IOException e) {
            throw new RuntimeException(e);
        }
        return availableLocales;
    }

    private static Locale getCurrentLocale(final @Size(min = 1) LocaleList localeList) {
        return localeList.get(0);
    }

    private static Optional<Locale> getFirstMatch(final LocaleList haystack, final List<Locale> needles) {
        return Optional.ofNullable(
                haystack.getFirstMatch(
                        toLanguageTags(needles).toArray(String[]::new)));
    }

    private static List<String> toLanguageTags(final List<Locale> locales) {
        return locales
                .stream()
                .map(Locale::toLanguageTag)
                .toList();
    }
}