package de.KnollFrank.lib.settingssearch.db.preference.dao;

import android.os.Bundle;

import com.google.common.collect.Sets;

import java.util.Set;

// FK-TODO: DRY with BundleEquality from unit test folder
public class BundleEquality {

    // adapted from https://stackoverflow.com/a/13238729
    public static boolean equalBundles(final Bundle bundle1, final Bundle bundle2) {
        if (bundle1.size() != bundle2.size()) {
            return false;
        }

        final Set<String> allKeys = Sets.union(bundle1.keySet(), bundle2.keySet());
        for (final String key : allKeys) {
            if (!bundle1.containsKey(key) || !bundle2.containsKey(key)) {
                return false;
            }
            final Object entry1 = bundle1.get(key);
            final Object entry2 = bundle2.get(key);
            if (entry1 instanceof final Bundle _bundle1
                    && entry2 instanceof final Bundle _bundle2
                    && !equalBundles(_bundle1, _bundle2)) {
                return false;
            } else if (entry1 == null) {
                if (entry2 != null) {
                    return false;
                }
            } else if (!entry1.equals(entry2)) {
                return false;
            }
        }
        return true;
    }
}