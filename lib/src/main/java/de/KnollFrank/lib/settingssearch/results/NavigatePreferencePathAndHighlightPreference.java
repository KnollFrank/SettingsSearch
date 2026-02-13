package de.KnollFrank.lib.settingssearch.results;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.fragment.navigation.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;

public class NavigatePreferencePathAndHighlightPreference implements INavigatePreferencePathAndHighlightPreference {

    private final PreferencePathNavigator preferencePathNavigator;
    private final PrepareShow prepareShow;
    private final ShowSettingsFragmentAndHighlightSetting showSettingsFragmentAndHighlightSetting;
    private final FragmentActivity activity;

    public NavigatePreferencePathAndHighlightPreference(final PreferencePathNavigator preferencePathNavigator,
                                                        final PrepareShow prepareShow,
                                                        final ShowSettingsFragmentAndHighlightSetting showSettingsFragmentAndHighlightSetting,
                                                        final FragmentActivity activity) {
        this.preferencePathNavigator = preferencePathNavigator;
        this.prepareShow = prepareShow;
        this.showSettingsFragmentAndHighlightSetting = showSettingsFragmentAndHighlightSetting;
        this.activity = activity;
    }

    // FK-TODO: add unit test which tries to navigate a preferencePath to a non existing SearchablePreferenceOfHostWithinTree (because the search database is not synchronized with the app's reality)
    @Override
    public void navigatePreferencePathAndHighlightPreference(final PreferencePath preferencePath) {
        preferencePathNavigator
                .navigatePreferencePath(preferencePath)
                .ifPresent(
                        fragmentOfPreferenceScreen ->
                                showSettingsFragmentAndHighlightSetting(
                                        fragmentOfPreferenceScreen,
                                        preferencePath.getEnd()));
    }

    private void showSettingsFragmentAndHighlightSetting(final Fragment settingsFragment,
                                                         final SearchablePreferenceOfHostWithinTree settingToHighlight) {
        prepareShow.prepareShow(settingsFragment);
        showSettingsFragmentAndHighlightSetting.showSettingsFragmentAndHighlightSetting(activity, settingsFragment, settingToHighlight);
    }
}
