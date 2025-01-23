package de.KnollFrank.lib.settingssearch.fragment.data;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

class PreferencePathData {

    private static final String PREFERENCE_PATH = PreferencePathData.class.getPackage().getName() + ".preferencePath";

    public static void putPreferencePathIds(final Bundle bundle, final List<Integer> preferencePathIds) {
        bundle.putIntegerArrayList(PREFERENCE_PATH, new ArrayList<>(preferencePathIds));
    }

    public static List<Integer> getPreferencePathIds(final Bundle bundle) {
        return bundle.getIntegerArrayList(PREFERENCE_PATH);
    }
}
