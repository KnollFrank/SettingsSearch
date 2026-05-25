package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.FragmentClassOfActivitySurrogate;

public record FragmentClassOfActivity<T extends Fragment>(
        Class<T> fragment,
        ActivityDescription activityOfFragment) {

    public FragmentClassOfActivitySurrogate asFragmentClassOfActivitySurrogate() {
        return new FragmentClassOfActivitySurrogate(
                fragment,
                activityOfFragment.asActivityDescriptionSurrogate());
    }
}
