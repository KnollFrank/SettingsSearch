package de.KnollFrank.lib.settingssearch.common.converter;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.google.gson.Gson;

import java.util.Optional;

public class BundleConverter {

    private static final Gson gson = new Gson();

    public static PersistableBundle toPersistableBundle(final Bundle bundle) {
        final PersistableBundle persistableBundle = new PersistableBundle();
        for (final String key : bundle.keySet()) {
            Optional
                    .ofNullable(bundle.get(key))
                    .ifPresentOrElse(
                            value -> putValueForKeyIntoBundle(key, value, persistableBundle),
                            () -> persistableBundle.putString(key, "<null>"));
        }
        return persistableBundle;
    }

    private static void putValueForKeyIntoBundle(final String key,
                                                 final Object value,
                                                 final PersistableBundle bundle) {
        if (value instanceof final String s) {
            bundle.putString(key, s);
        } else if (value instanceof final Integer i) {
            bundle.putInt(key, i);
        } else if (value instanceof final Long l) {
            bundle.putLong(key, l);
        } else if (value instanceof final Double d) {
            bundle.putDouble(key, d);
        } else if (value instanceof final Boolean b) {
            bundle.putBoolean(key, b);
        } else if (value instanceof final String[] sa) {
            bundle.putStringArray(key, sa);
        } else if (value instanceof final int[] ia) {
            bundle.putIntArray(key, ia);
        } else if (value instanceof final long[] la) {
            bundle.putLongArray(key, la);
        } else if (value instanceof final double[] da) {
            bundle.putDoubleArray(key, da);
        } else if (value instanceof final boolean[] ba) {
            bundle.putBooleanArray(key, ba);
        } else if (value instanceof final PersistableBundle pb) {
            bundle.putPersistableBundle(key, pb);
        } else if (value instanceof final Bundle b) {
            bundle.putPersistableBundle(key, toPersistableBundle(b));
        } else {
            try {
                bundle.putString(key, gson.toJson(value));
            } catch (final Exception e) {
                bundle.putString(key, "<Unserializable type: " + value.getClass().getName() + ">");
            }
        }
    }
}
