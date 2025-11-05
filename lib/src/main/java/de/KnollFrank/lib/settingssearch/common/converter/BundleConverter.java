package de.KnollFrank.lib.settingssearch.common.converter;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.google.gson.Gson;

public class BundleConverter {

    private static final Gson gson = new Gson();

    public static PersistableBundle toPersistableBundle(final Bundle bundle) {
        final PersistableBundle persistableBundle = new PersistableBundle();
        for (final String key : bundle.keySet()) {
            final Object value = bundle.get(key);
            if (value == null) {
                persistableBundle.putString(key, "<null>");
                continue;
            }
            if (value instanceof final String s) {
                persistableBundle.putString(key, s);
            } else if (value instanceof final Integer i) {
                persistableBundle.putInt(key, i);
            } else if (value instanceof final Long l) {
                persistableBundle.putLong(key, l);
            } else if (value instanceof final Double d) {
                persistableBundle.putDouble(key, d);
            } else if (value instanceof final Boolean b) {
                persistableBundle.putBoolean(key, b);
            } else if (value instanceof final String[] sa) {
                persistableBundle.putStringArray(key, sa);
            } else if (value instanceof final int[] ia) {
                persistableBundle.putIntArray(key, ia);
            } else if (value instanceof final long[] la) {
                persistableBundle.putLongArray(key, la);
            } else if (value instanceof final double[] da) {
                persistableBundle.putDoubleArray(key, da);
            } else if (value instanceof final boolean[] ba) {
                persistableBundle.putBooleanArray(key, ba);
            } else if (value instanceof final PersistableBundle pb) {
                persistableBundle.putPersistableBundle(key, pb);
            } else if (value instanceof final Bundle b) {
                persistableBundle.putPersistableBundle(key, toPersistableBundle(b));
            } else {
                try {
                    persistableBundle.putString(key, gson.toJson(value));
                } catch (final Exception e) {
                    persistableBundle.putString(key, "<Unserializable type: " + value.getClass().getName() + ">");
                }
            }
        }
        return persistableBundle;
    }
}
