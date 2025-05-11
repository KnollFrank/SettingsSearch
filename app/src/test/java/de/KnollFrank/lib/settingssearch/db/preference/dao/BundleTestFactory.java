package de.KnollFrank.lib.settingssearch.db.preference.dao;

import android.os.Bundle;

public class BundleTestFactory {

    public static Bundle createBundle(final String key, final String value) {
        final Bundle bundle = new Bundle();
        bundle.putString(key, value);
        return bundle;
    }

    public static Bundle createSomeBundleOfBundles() {
        final Bundle outer = new Bundle();
        outer.putString("a", "abc");

        final Bundle inner = new Bundle();
        inner.putString("b", "bcd");

        outer.putBundle("c", inner);
        return outer;
    }
}
