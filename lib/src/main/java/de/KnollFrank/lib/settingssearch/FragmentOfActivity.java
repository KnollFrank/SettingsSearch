package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;

// FK-TODO: vereinheitliche mit PreferenceFragmentOfActivity
public record FragmentOfActivity<T extends Fragment>(
        T fragment,
        ActivityDescription activityOfFragment) {

    public FragmentClassOfActivity<? extends Fragment> asFragmentClassOfActivity() {
        return new FragmentClassOfActivity<>(
                fragment.getClass(),
                activityOfFragment);
    }
}
