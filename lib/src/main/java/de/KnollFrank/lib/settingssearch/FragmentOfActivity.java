package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;

public record FragmentOfActivity<T extends Fragment>(
        T fragment,
        ActivityDescription activityOfFragment) {

    public FragmentClassOfActivity<T> asFragmentClassOfActivity() {
        return new FragmentClassOfActivity<>(
                (Class<T>) fragment.getClass(),
                activityOfFragment);
    }
}
