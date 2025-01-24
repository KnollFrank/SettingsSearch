package de.KnollFrank.lib.settingssearch.fragment.data;

import android.os.Bundle;

import java.util.List;

// FK-TODO: es reicht, die id der SearchablePreference zu speichern, statt preferencePathIds, da in der DB der PreferencePath zu dieser SearchablePreference hinterlegt ist.
public record PreferencePathNavigatorData(List<Integer> preferencePathIds,
                                          int indexWithinPreferencePath) {

    public Bundle toBundle() {
        final Bundle bundle = new Bundle();
        PreferencePathData.putPreferencePathIds(bundle, preferencePathIds());
        IndexWithinPreferencePathData.putIndexWithinPreferencePath(bundle, indexWithinPreferencePath());
        return bundle;
    }

    public static PreferencePathNavigatorData fromBundle(final Bundle bundle) {
        return new PreferencePathNavigatorData(
                PreferencePathData.getPreferencePathIds(bundle),
                IndexWithinPreferencePathData.getIndexWithinPreferencePath(bundle));
    }
}
