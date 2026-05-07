package de.KnollFrank.lib.settingssearch.results;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.fragment.navigation.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;

public class NavigatePreferencePathAndHighlightPreference implements INavigatePreferencePathAndHighlightPreference {

    private final PrepareShow prepareShow;
    private final ShowSettingsFragmentAndHighlightSetting showSettingsFragmentAndHighlightSetting;
    private final FragmentActivity activity;

    public NavigatePreferencePathAndHighlightPreference(final PrepareShow prepareShow,
                                                        final ShowSettingsFragmentAndHighlightSetting showSettingsFragmentAndHighlightSetting,
                                                        final FragmentActivity activity) {
        this.prepareShow = prepareShow;
        this.showSettingsFragmentAndHighlightSetting = showSettingsFragmentAndHighlightSetting;
        this.activity = activity;
    }

    // FK-TODO: add unit test which tries to navigate a preferencePath to a non existing SearchablePreferenceOfHostWithinTree (because the search database is not synchronized with the app's reality)
    @Override
    public void navigatePreferencePathAndHighlightPreference(final PreferencePath preferencePath) {
        Futures.addCallback(
                PreferencePathNavigator.navigatePreferencePath(preferencePath),
                new FutureCallback<>() {

                    @Override
                    public void onSuccess(final Optional<? extends Fragment> result) {
                        result.ifPresent(fragment -> highlightSetting(fragment, preferencePath.getEnd()));
                    }

                    @Override
                    public void onFailure(@NonNull final Throwable t) {
                        throw new RuntimeException("UI Automator navigation failed", t);
                    }
                },
                ContextCompat.getMainExecutor(activity));
    }

    private void highlightSetting(final Fragment settingsFragment,
                                  final SearchablePreferenceOfHostWithinTree settingToHighlight) {
        prepareShow.prepareShow(settingsFragment);
        showSettingsFragmentAndHighlightSetting.showSettingsFragmentAndHighlightSetting(activity, settingsFragment, settingToHighlight);
    }
}
