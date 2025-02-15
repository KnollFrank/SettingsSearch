package de.KnollFrank.lib.settingssearch.results;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.threeten.bp.Duration;

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
                _settingsFragment ->
                        // FK-TODO: refactor
                        setting2Highlight.getKey().ifPresent(keyOfPreference2Highlight -> {
                            if (_settingsFragment instanceof final PreferenceFragmentCompat fragmentOfPreferenceScreen) {
                                highlightPreference(fragmentOfPreferenceScreen, keyOfPreference2Highlight);
                                showDialog(fragmentOfPreferenceScreen.findPreference(keyOfPreference2Highlight), setting2Highlight);
                            } else if (_settingsFragment instanceof final SettingsFragment __settingsFragment) {
                                highlightSetting(__settingsFragment, keyOfPreference2Highlight);
                                // showDialog(_settingsFragment.findPreference(keyOfSetting2Highlight), setting2Highlight);
                            }
                        }),
                true,
                fragmentContainerViewId,
                Optional.empty(),
                fragmentManager);
    }

    private static void highlightPreference(final PreferenceFragmentCompat preferenceFragment,
                                            final String keyOfPreference2Highlight) {
        preferenceFragment.scrollToPreference(keyOfPreference2Highlight);
        PreferenceHighlighter.highlightPreferenceOfPreferenceFragment(
                keyOfPreference2Highlight,
                preferenceFragment,
                Duration.ofSeconds(1));
    }

    private static void highlightSetting(final SettingsFragment settingsFragment,
                                         final String keyOfSetting2Highlight) {
        ItemOfRecyclerViewHighlighter.highlightItemOfRecyclerView(
                settingsFragment.getPositionOfSetting(keyOfSetting2Highlight),
                settingsFragment.getRecyclerView(),
                Duration.ofSeconds(1));
    }

    private static void showDialog(final Preference preference, final SearchablePreference searchablePreference) {
        if (!searchablePreference.hasPreferenceMatchWithinSearchableInfo()) {
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
