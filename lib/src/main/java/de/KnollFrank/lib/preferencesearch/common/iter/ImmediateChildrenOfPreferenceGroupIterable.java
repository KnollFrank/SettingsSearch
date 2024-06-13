package de.KnollFrank.lib.preferencesearch.common.iter;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;

import java.util.Iterator;

class ImmediateChildrenOfPreferenceGroupIterable implements Iterable<Preference> {

    private final PreferenceGroup preferenceGroup;

    public ImmediateChildrenOfPreferenceGroupIterable(final PreferenceGroup preferenceGroup) {
        this.preferenceGroup = preferenceGroup;
    }

    @NonNull
    @Override
    public Iterator<Preference> iterator() {
        return new ImmediateChildrenOfPreferenceGroupIterator(preferenceGroup);
    }
}
