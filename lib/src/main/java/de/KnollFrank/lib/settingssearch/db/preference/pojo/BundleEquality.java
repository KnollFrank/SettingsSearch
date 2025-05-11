package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.Bundle;

public class BundleEquality {

    public static boolean equalBundles(final Bundle bundle1, final Bundle bundle2) {
        return new BundleWithEquality(bundle1).equals(new BundleWithEquality(bundle2));
    }
}