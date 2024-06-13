package de.KnollFrank.lib.preferencesearch.common.iter;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;

import java.util.Iterator;

class ImmediateChildrenOfPreferenceGroupIterator implements Iterator<Preference> {

    private final PreferenceGroup preferenceGroup;
    private int i = 0;

    public ImmediateChildrenOfPreferenceGroupIterator(final PreferenceGroup preferenceGroup) {
        this.preferenceGroup = preferenceGroup;
    }

    @Override
    public boolean hasNext() {
        return i < preferenceGroup.getPreferenceCount();
    }

    @Override
    public Preference next() {
        return preferenceGroup.getPreference(i++);
    }
}
