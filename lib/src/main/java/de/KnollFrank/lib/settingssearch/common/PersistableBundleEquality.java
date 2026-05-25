package de.KnollFrank.lib.settingssearch.common;

import android.os.PersistableBundle;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class PersistableBundleEquality {

    private PersistableBundleEquality() {
    }

    public static boolean areBundlesEqual(@Nullable final PersistableBundle one, @Nullable final PersistableBundle two) {
        if (one == two) {
            return true;
        }
        if (one == null || two == null) {
            return false;
        }
        if (one.size() != two.size()) {
            return false;
        }
        for (final String key : one.keySet()) {
            if (!two.containsKey(key)) {
                return false;
            }
            if (!areObjectsEqual(one.get(key), two.get(key))) {
                return false;
            }
        }
        return true;
    }

    // FK-TODO: refactor
    public static int hashCode(@Nullable final PersistableBundle bundle) {
        if (bundle == null) {
            return 0;
        }
        int res = 0;
        for (final String key : bundle.keySet()) {
            res = 31 * res + Objects.hashCode(key);
            final Object value = bundle.get(key);
            if (value instanceof final PersistableBundle sub) {
                res = 31 * res + hashCode(sub);
            } else if (value != null && value.getClass().isArray()) {
                if (value instanceof final int[] ints) {
                    res = 31 * res + Arrays.hashCode(ints);
                } else if (value instanceof final long[] longs) {
                    res = 31 * res + Arrays.hashCode(longs);
                } else if (value instanceof final double[] doubles) {
                    res = 31 * res + Arrays.hashCode(doubles);
                } else if (value instanceof final boolean[] booleans) {
                    res = 31 * res + Arrays.hashCode(booleans);
                } else if (value instanceof final Object[] objects) {
                    res = 31 * res + Arrays.deepHashCode(objects);
                }
            } else {
                res = 31 * res + Objects.hashCode(value);
            }
        }
        return res;
    }

    private static boolean areObjectsEqual(@Nullable final Object a, @Nullable final Object b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.getClass().isArray() && b.getClass().isArray()) {
            if (a instanceof final int[] intsA && b instanceof final int[] intsB) {
                return Arrays.equals(intsA, intsB);
            } else if (a instanceof final long[] longsA && b instanceof final long[] longsB) {
                return Arrays.equals(longsA, longsB);
            } else if (a instanceof final double[] doublesA && b instanceof final double[] doublesB) {
                return Arrays.equals(doublesA, doublesB);
            } else if (a instanceof final boolean[] booleansA && b instanceof final boolean[] booleansB) {
                return Arrays.equals(booleansA, booleansB);
            } else if (a instanceof final Object[] objectsA && b instanceof final Object[] objectsB) {
                return Arrays.deepEquals(objectsA, objectsB);
            }
        }
        if (a instanceof final PersistableBundle bundleA && b instanceof final PersistableBundle bundleB) {
            return areBundlesEqual(bundleA, bundleB);
        }
        return a.equals(b);
    }
}
