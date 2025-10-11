package de.KnollFrank.lib.settingssearch.common;

import android.content.res.Resources;

import com.google.common.base.Suppliers;

import java.util.Locale;
import java.util.function.Supplier;

public class Utils {

    public static <T> Supplier<T> memoize(final Supplier<T> supplier) {
        return Suppliers.memoize(supplier::get)::get;
    }

    public static Locale getCurrentLanguageLocale(final Resources resources) {
        return getLanguageLocale(getCurrentLocale(resources));
    }

    private static Locale getCurrentLocale(final Resources resources) {
        return resources.getConfiguration().getLocales().get(0);
    }

    private static Locale getLanguageLocale(final Locale locale) {
        return new Locale(locale.getLanguage());
    }
}
