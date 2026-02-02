package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.ActivityDescription;

public record FragmentClassOfActivity(
        Class<? extends Fragment> fragment,
        ActivityDescription activityOFragment) {
}
