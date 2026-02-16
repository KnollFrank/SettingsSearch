package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceFragmentClassOfActivitySurrogate;

public record FragmentClassOfActivity<T extends Fragment>(
        Class<T> fragment,
        ActivityDescription activityOfFragment) {

    public PreferenceFragmentClassOfActivitySurrogate asPreferenceFragmentClassOfActivitySurrogate() {
        return new PreferenceFragmentClassOfActivitySurrogate(
                asPreferenceFragmentClass(),
                activityOfFragment.asActivityDescriptionSurrogate());
    }

    private Class<? extends PreferenceFragmentCompat> asPreferenceFragmentClass() {
        if (!PreferenceFragmentCompat.class.isAssignableFrom(fragment)) {
            throw new IllegalArgumentException("The fragment class must be a subclass of PreferenceFragmentCompat. But it is of type: " + fragment.getName());
        }
        @SuppressWarnings("unchecked") final Class<? extends PreferenceFragmentCompat> preferenceFragmentClass = (Class<? extends PreferenceFragmentCompat>) fragment;
        return preferenceFragmentClass;
    }
}
