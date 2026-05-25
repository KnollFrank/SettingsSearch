package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.fragment.app.Fragment;
import androidx.room.Embedded;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;

public record FragmentClassOfActivitySurrogate(
        Class<? extends Fragment> fragment,
        @Embedded ActivityDescriptionSurrogate activityOfFragment) {

    public FragmentClassOfActivity<? extends Fragment> asFragmentClassOfActivity() {
        return new FragmentClassOfActivity<>(
                fragment,
                activityOfFragment.asActivityDescription());
    }
}
