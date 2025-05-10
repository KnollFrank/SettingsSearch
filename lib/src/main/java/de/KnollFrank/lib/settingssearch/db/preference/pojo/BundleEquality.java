package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.Bundle;

import java.util.HashSet;
import java.util.Set;

public class BundleEquality {

    // FK-TODO: unit test and refactor
    // adapted from https://stackoverflow.com/a/13238729
    public static boolean equalBundles(final Bundle one, final Bundle two) {
        if (one.size() != two.size())
            return false;

        Set<String> setOne = new HashSet<>(one.keySet());
        setOne.addAll(two.keySet());
        Object valueOne;
        Object valueTwo;

        for (String key : setOne) {
            if (!one.containsKey(key) || !two.containsKey(key))
                return false;

            valueOne = one.get(key);
            valueTwo = two.get(key);
            if (valueOne instanceof Bundle && valueTwo instanceof Bundle &&
                    !equalBundles((Bundle) valueOne, (Bundle) valueTwo)) {
                return false;
            } else if (valueOne == null) {
                if (valueTwo != null)
                    return false;
            } else if (!valueOne.equals(valueTwo))
                return false;
        }

        return true;
    }
}