package de.KnollFrank.lib.preferencesearch.results;

import android.os.Bundle;

class Bundles {

    private static final String PREFERENCE_KEY = Bundles.class.getName() + ".preferenceKey";

    public static Bundle preferenceKey2Bundle(final String preferenceKey) {
        final Bundle arguments = new Bundle();
        arguments.putString(PREFERENCE_KEY, preferenceKey);
        return arguments;
    }

    public static String bundle2PreferenceKey(final Bundle arguments) {
        return arguments.getString(PREFERENCE_KEY);
    }
}
