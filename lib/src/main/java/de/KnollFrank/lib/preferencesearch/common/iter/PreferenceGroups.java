package de.KnollFrank.lib.preferencesearch.common.iter;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;

public class PreferenceGroups {

    public static Iterable<Preference> iterateOverImmediateChildrenOf(final PreferenceGroup preferenceGroup) {
        return new ImmediateChildrenOfPreferenceGroupIterable(preferenceGroup);
    }
}
