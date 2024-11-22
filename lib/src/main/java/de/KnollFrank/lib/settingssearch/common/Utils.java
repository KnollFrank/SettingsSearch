package de.KnollFrank.lib.settingssearch.common;

import android.content.res.Resources;

import com.google.common.base.Suppliers;

import java.util.Locale;
import java.util.function.Supplier;

public class Utils {

    public static <T> Class<? extends T> getClass(final String className) {
        try {
            return (Class<? extends T>) Class.forName(className);
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> Supplier<T> memoize(final Supplier<T> supplier) {
        return Suppliers.memoize(supplier::get)::get;
    }

    public static Locale geCurrentLocale(final Resources resources) {
        return resources.getConfiguration().getLocales().get(0);
    }
}
