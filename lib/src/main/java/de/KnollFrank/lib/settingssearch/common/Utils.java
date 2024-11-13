package de.KnollFrank.lib.settingssearch.common;

import android.content.res.Resources;

import java.util.Locale;

public class Utils {

    public static <T> Class<? extends T> getClass(final String className) {
        try {
            return (Class<? extends T>) Class.forName(className);
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Locale geCurrentLocale(final Resources resources) {
        return resources.getConfiguration().getLocales().get(0);
    }
}
