package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;

public record FragmentClassOfActivity<T extends Fragment>(
        Class<T> fragment,
        ActivityDescription activityOFragment) {
}