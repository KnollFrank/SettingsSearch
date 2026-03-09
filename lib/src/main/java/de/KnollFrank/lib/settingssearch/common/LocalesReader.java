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

public class LocalesReader {

    // FK-TODO: refactor
    public static List<Locale> readLocales(final Resources resources, final @XmlRes int resId) {
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
}
