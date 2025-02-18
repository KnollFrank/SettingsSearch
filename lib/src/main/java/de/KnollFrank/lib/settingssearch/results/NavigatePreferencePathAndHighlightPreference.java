package de.KnollFrank.lib.settingssearch.results;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.fragment.navigation.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.fragment.navigation.PreferencePathPointer;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;

public class NavigatePreferencePathAndHighlightPreference implements INavigatePreferencePathAndHighlightPreference {

    private final PreferencePathNavigator preferencePathNavigator;
    private final @IdRes int fragmentContainerViewId;
    private final PrepareShow prepareShow;
    private final FragmentManager fragmentManager;

    public NavigatePreferencePathAndHighlightPreference(final PreferencePathNavigator preferencePathNavigator,
                                                        final @IdRes int fragmentContainerViewId,
                                                        final PrepareShow prepareShow,
                                                        final FragmentManager fragmentManager) {
        this.preferencePathNavigator = preferencePathNavigator;
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.prepareShow = prepareShow;
        this.fragmentManager = fragmentManager;
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
        showFragment(
                settingsFragment,
                _settingsFragment -> highlightSetting(_settingsFragment, setting2Highlight),
                true,
                fragmentContainerViewId,
                Optional.empty(),
                fragmentManager);
    }

    private static void highlightSetting(final Fragment settingsFragment,
                                         final SearchablePreference setting2Highlight) {
        // FK-TODO: refactor
        if (settingsFragment instanceof final PreferenceFragmentCompat fragmentOfPreferenceScreen) {
            highlightPreference(fragmentOfPreferenceScreen, asSetting(setting2Highlight), setting2Highlight.hasPreferenceMatchWithinSearchableInfo());
        } else if (settingsFragment instanceof final SettingHighlighterProvider settingHighlighterProvider) {
            highlightSetting(
                    settingsFragment,
                    asSetting(setting2Highlight),
                    settingHighlighterProvider);
        }
    }

    private static Setting asSetting(final SearchablePreference searchablePreference) {
        return new Setting() {

            @Override
            public String getKey() {
                return searchablePreference.getKey();
            }
        };
    }

    private static void highlightPreference(final PreferenceFragmentCompat fragmentOfPreferenceScreen,
                                            final Setting setting,
                                            final boolean hasPreferenceMatchWithinSearchableInfo) {
        fragmentOfPreferenceScreen.scrollToPreference(setting.getKey());
        new PreferenceHighlighter().highlightSetting(fragmentOfPreferenceScreen, setting);
        showDialog(fragmentOfPreferenceScreen.findPreference(setting.getKey()), hasPreferenceMatchWithinSearchableInfo);
    }

    private static void highlightSetting(final Fragment settingsFragment,
                                         final Setting setting2Highlight,
                                         final SettingHighlighterProvider settingHighlighterProvider) {
        settingHighlighterProvider.getSettingHighlighter().highlightSetting(settingsFragment, setting2Highlight);
        // showDialog(_settingsFragment.findPreference(keyOfSetting2Highlight), setting2Highlight);
    }

    private static void showDialog(final Preference preference, final boolean hasPreferenceMatchWithinSearchableInfo) {
        if (!hasPreferenceMatchWithinSearchableInfo) {
            return;
        }
        if (preference instanceof final DialogPreference dialogPreference) {
            dialogPreference.getPreferenceManager().showDialog(dialogPreference);
        } else if (preference.getOnPreferenceClickListener() != null) {
            // FK-TODO: or use "preference.performClick();" instead?
            preference.getOnPreferenceClickListener().onPreferenceClick(preference);
        }
    }
}
