package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.os.Bundle;

import java.util.Optional;

class PreferencePathNavigatorDataConverter {

    private static final String PREFERENCE_PATH_NAVIGATOR_DATA = PreferencePathNavigatorData.class.getPackage().getName();

    public static Bundle toBundle(final PreferencePathNavigatorData preferencePathNavigatorData) {
        final Bundle bundle = new Bundle();
        bundle.putBundle(PREFERENCE_PATH_NAVIGATOR_DATA, new Converter().doForward(preferencePathNavigatorData));
        return bundle;
    }

    public static Optional<PreferencePathNavigatorData> fromBundle(final Bundle bundle) {
        return Optional
                .ofNullable(bundle.getBundle(PREFERENCE_PATH_NAVIGATOR_DATA))
                .map(new Converter()::doBackward);
    }

    private static class Converter implements de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.Converter<PreferencePathNavigatorData, Bundle> {

        private static final String ID_OF_SEARCHABLE_PREFERENCE = "idOfSearchablePreference";
        private static final String INDEX_WITHIN_PREFERENCE_PATH = "indexWithinPreferencePath";

        @Override
        public Bundle doForward(final PreferencePathNavigatorData preferencePathNavigatorData) {
            final Bundle bundle = new Bundle();
            bundle.putString(ID_OF_SEARCHABLE_PREFERENCE, preferencePathNavigatorData.idOfSearchablePreference());
            bundle.putInt(INDEX_WITHIN_PREFERENCE_PATH, preferencePathNavigatorData.indexWithinPreferencePath());
            return bundle;
        }

        @Override
        public PreferencePathNavigatorData doBackward(final Bundle bundle) {
            return new PreferencePathNavigatorData(
                    bundle.getString(ID_OF_SEARCHABLE_PREFERENCE),
                    bundle.getInt(INDEX_WITHIN_PREFERENCE_PATH));
        }
    }
}
