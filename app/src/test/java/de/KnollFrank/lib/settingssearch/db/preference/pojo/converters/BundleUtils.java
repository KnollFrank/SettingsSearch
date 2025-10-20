package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import android.os.PersistableBundle;

import java.util.Arrays;

class BundleUtils {

    public static boolean areBundlesEqual(final PersistableBundle one, final PersistableBundle two) {
        if (one == two) {
            return true;
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

    private static boolean areObjectsEqual(final Object a, final Object b) {
        if (a == b) {
            return true;
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
