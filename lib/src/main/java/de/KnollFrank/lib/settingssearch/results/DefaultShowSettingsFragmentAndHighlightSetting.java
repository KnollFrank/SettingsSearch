package de.KnollFrank.lib.settingssearch.results;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;

public class DefaultShowSettingsFragmentAndHighlightSetting implements ShowSettingsFragmentAndHighlightSetting {

    @Override
    public void showSettingsFragmentAndHighlightSetting(final FragmentActivity activity,
                                                        final Fragment settingsFragment,
                                                        final SearchablePreferenceOfHostWithinTree settingToHighlight) {
        highlightSetting(settingsFragment, asSetting(settingToHighlight));
    }

    private static void highlightSetting(final Fragment settingsFragment, final Setting setting) {
        if (settingsFragment instanceof final PreferenceFragmentCompat fragmentOfPreferenceScreen) {
            highlightPreference(fragmentOfPreferenceScreen, setting);
        } else if (settingsFragment instanceof final SettingHighlighterProvider settingHighlighterProvider) {
            highlightSetting(settingsFragment, setting, settingHighlighterProvider.getSettingHighlighter());
        }
    }

    private static Setting asSetting(final SearchablePreferenceOfHostWithinTree preference) {
        return new Setting() {

            @Override
            public String getKey() {
                return preference.searchablePreference().getKey();
            }

            @Override
            public boolean hasPreferenceMatchWithinSearchableInfo() {
                return preference.searchablePreference().hasPreferenceMatchWithinSearchableInfo();
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
                                         final Setting settingToHighlight,
                                         final SettingHighlighter settingHighlighter) {
        settingHighlighter.highlightSetting(settingsFragment, settingToHighlight);
        // showDialog(_settingsFragment.findPreference(keyOfSettingToHighlight), settingToHighlight);
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
