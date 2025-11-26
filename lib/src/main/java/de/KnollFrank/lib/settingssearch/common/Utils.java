package de.KnollFrank.lib.settingssearch.common;

import com.google.common.base.Suppliers;

import java.util.function.Supplier;

public class Utils {

    public static <T> Supplier<T> memoize(final Supplier<T> supplier) {
        return Suppliers.memoize(supplier::get)::get;
    }
}
