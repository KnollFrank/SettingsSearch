package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.os.Bundle;

import java.util.Optional;

class PreferencePathNavigatorDataConverter {

    private static final String PREFERENCE_PATH_NAVIGATOR_DATA = PreferencePathNavigatorData.class.getPackage().getName();

    public static Bundle toBundle(final PreferencePathNavigatorData preferencePathNavigatorData) {
        final Bundle bundle = new Bundle();
        bundle.putBundle(PREFERENCE_PATH_NAVIGATOR_DATA, Converter.toBundle(preferencePathNavigatorData));
        return bundle;
    }

    public static Optional<PreferencePathNavigatorData> fromBundle(final Bundle bundle) {
        return Optional
                .ofNullable(bundle.getBundle(PREFERENCE_PATH_NAVIGATOR_DATA))
                .map(Converter::fromBundle);
    }

    private static class Converter {

        private static final String ID_OF_SEARCHABLE_PREFERENCE = "idOfSearchablePreference";
        private static final String INDEX_WITHIN_PREFERENCE_PATH = "indexWithinPreferencePath";

        public static Bundle toBundle(final PreferencePathNavigatorData preferencePathNavigatorData) {
            final Bundle bundle = new Bundle();
            bundle.putInt(ID_OF_SEARCHABLE_PREFERENCE, preferencePathNavigatorData.idOfSearchablePreference());
            bundle.putInt(INDEX_WITHIN_PREFERENCE_PATH, preferencePathNavigatorData.indexWithinPreferencePath());
            return bundle;
        }

        public static PreferencePathNavigatorData fromBundle(final Bundle bundle) {
            return new PreferencePathNavigatorData(
                    bundle.getInt(ID_OF_SEARCHABLE_PREFERENCE),
                    bundle.getInt(INDEX_WITHIN_PREFERENCE_PATH));
        }
    }
}
