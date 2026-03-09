package de.KnollFrank.lib.settingssearch.common;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import androidx.annotation.XmlRes;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.LocaleConverter;

public class LocalesReader {

    private LocalesReader() {
    }

    public static List<Locale> readLocales(final Resources resources, final @XmlRes int resId) {
        return LocaleConverter.getLocales(getLocaleNames(resources, resId));
    }

    private static List<String> getLocaleNames(final Resources resources, final @XmlRes int resId) {
        final List<String> localeNames = new ArrayList<>();
        try (final XmlResourceParser parser = resources.getXml(resId)) {
            for (int eventType = parser.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = parser.next()) {
                if (eventType == XmlPullParser.START_TAG && "locale".equals(parser.getName())) {
                    final String localeName = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "name");
                    localeNames.add(localeName);
                }
            }
        } catch (final XmlPullParserException | IOException e) {
            throw new RuntimeException(e);
        }
        return localeNames;
    }
}
