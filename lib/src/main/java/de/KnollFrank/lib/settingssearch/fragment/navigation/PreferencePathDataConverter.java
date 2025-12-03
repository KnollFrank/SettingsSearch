package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Optional;

class PreferencePathDataConverter {

    private static final String PREFERENCE_PATH_DATA = PreferencePathData.class.getPackage().getName();

    public static Bundle toBundle(final PreferencePathData preferencePathData) {
        final Bundle bundle = new Bundle();
        bundle.putBundle(PREFERENCE_PATH_DATA, new Converter().convertForward(preferencePathData));
        return bundle;
    }

    public static Optional<PreferencePathData> fromBundle(final Bundle bundle) {
        return Optional
                .ofNullable(bundle.getBundle(PREFERENCE_PATH_DATA))
                .map(new Converter()::convertBackward);
    }

    private static class Converter implements de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.Converter<PreferencePathData, Bundle> {

        private static final String PREFERENCE_IDS = "preferenceIds";

        @Override
        public Bundle convertForward(final PreferencePathData preferencePathData) {
            final Bundle bundle = new Bundle();
            bundle.putStringArrayList(PREFERENCE_IDS, new ArrayList<>(preferencePathData.preferenceIds()));
            return bundle;
        }

        @Override
        public PreferencePathData convertBackward(final Bundle bundle) {
            return new PreferencePathData(bundle.getStringArrayList(PREFERENCE_IDS));
        }
    }
}
