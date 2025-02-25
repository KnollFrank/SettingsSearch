package de.KnollFrank.lib.settingssearch.results;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.fragment.navigation.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.fragment.navigation.PreferencePathPointer;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;

public class NavigatePreferencePathAndHighlightPreference implements INavigatePreferencePathAndHighlightPreference {

    private final PreferencePathNavigator preferencePathNavigator;
    private final PrepareShow prepareShow;
    private final ShowSettingsFragmentAndHighlightSetting showSettingsFragmentAndHighlightSetting;
    private final Activity activity;

    public NavigatePreferencePathAndHighlightPreference(final PreferencePathNavigator preferencePathNavigator,
                                                        final PrepareShow prepareShow,
                                                        final ShowSettingsFragmentAndHighlightSetting showSettingsFragmentAndHighlightSetting,
                                                        final Activity activity) {
        this.preferencePathNavigator = preferencePathNavigator;
        this.prepareShow = prepareShow;
        this.showSettingsFragmentAndHighlightSetting = showSettingsFragmentAndHighlightSetting;
        this.activity = activity;
    }

    @Override
    public void navigatePreferencePathAndHighlightPreference(final PreferencePathPointer preferencePathPointer) {
        preferencePathNavigator
                .navigatePreferencePath(preferencePathPointer)
                .ifPresent(
                        fragmentOfPreferenceScreen ->
                                showSettingsFragmentAndHighlightSetting(
                                        fragmentOfPreferenceScreen,
                                        preferencePathPointer.preferencePath.getPreference()));
    }

    private void showSettingsFragmentAndHighlightSetting(final Fragment settingsFragment,
                                                         final SearchablePreference setting2Highlight) {
        prepareShow.prepareShow(settingsFragment);
        showSettingsFragmentAndHighlightSetting.showSettingsFragmentAndHighlightSetting(activity, settingsFragment, setting2Highlight);
    }
}
