package de.KnollFrank.lib.settingssearch.fragment.data;

import android.os.Bundle;

class IndexWithinPreferencePathData {

    private static final String INDEX_WITHIN_PREFERENCE_PATH = IndexWithinPreferencePathData.class.getPackage().getName() + ".indexWithinPreferencePath";

    public static void putIndexWithinPreferencePath(final Bundle bundle, final int indexWithinPreferencePath) {
        bundle.putInt(INDEX_WITHIN_PREFERENCE_PATH, indexWithinPreferencePath);
    }

    public static int getIndexWithinPreferencePath(final Bundle bundle) {
        return bundle.getInt(INDEX_WITHIN_PREFERENCE_PATH);
    }
}
