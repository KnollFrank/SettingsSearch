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

public class DefaultShowSettingsFragmentAndHighlightSetting implements ShowSettingsFragmentAndHighlightSetting {

    private final @IdRes int fragmentContainerViewId;
    private final FragmentManager fragmentManager;

    public DefaultShowSettingsFragmentAndHighlightSetting(final @IdRes int fragmentContainerViewId,
                                                          final FragmentManager fragmentManager) {
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void showSettingsFragmentAndHighlightSetting(final Fragment settingsFragment, final SearchablePreference setting2Highlight) {
        showFragment(
                settingsFragment,
                _settingsFragment -> highlightSetting(_settingsFragment, asSetting(setting2Highlight)),
                true,
                fragmentContainerViewId,
                Optional.empty(),
                fragmentManager);
    }

    private static void highlightSetting(final Fragment settingsFragment, final Setting setting) {
        if (settingsFragment instanceof final PreferenceFragmentCompat fragmentOfPreferenceScreen) {
            highlightPreference(fragmentOfPreferenceScreen, setting);
        } else if (settingsFragment instanceof final SettingHighlighterProvider settingHighlighterProvider) {
            highlightSetting(settingsFragment, setting, settingHighlighterProvider.getSettingHighlighter());
        }
    }

    private static Setting asSetting(final SearchablePreference preference) {
        return new Setting() {

            @Override
            public String getKey() {
                return preference.getKey();
            }

            @Override
            public boolean hasPreferenceMatchWithinSearchableInfo() {
                return preference.hasPreferenceMatchWithinSearchableInfo();
            }
        };
    }

    private static void highlightPreference(final PreferenceFragmentCompat fragmentOfPreferenceScreen,
                                            final Setting setting) {
        fragmentOfPreferenceScreen.scrollToPreference(setting.getKey());
        new PreferenceHighlighter().highlightSetting(fragmentOfPreferenceScreen, setting);
        showDialog(fragmentOfPreferenceScreen.findPreference(setting.getKey()), setting.hasPreferenceMatchWithinSearchableInfo());
    }

    private static void highlightSetting(final Fragment settingsFragment,
                                         final Setting setting2Highlight,
                                         final SettingHighlighter settingHighlighter) {
        settingHighlighter.highlightSetting(settingsFragment, setting2Highlight);
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
