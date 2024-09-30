package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.settingssearch.common.Preferences;

class PreferenceScreenCopier {

    private final PreferenceManager preferenceManager;

    public PreferenceScreenCopier(final PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    public PreferenceScreen copy(final PreferenceScreen preferenceScreen) {
        final PreferenceScreen copy = createPreferenceScreen(preferenceScreen);
        copySrc2Dst(preferenceScreen, copy);
        return copy;
    }

    private PreferenceScreen createPreferenceScreen(final PreferenceScreen preferenceScreen) {
        final PreferenceScreen copy = preferenceManager.createPreferenceScreen(preferenceManager.getContext());
        copy.setTitle(preferenceScreen.getTitle());
        return copy;
    }

    private static <T extends PreferenceGroup> void copySrc2Dst(final PreferenceGroup src, final T dst) {
        for (final Preference child : Preferences.getDirectChildren(src)) {
            if (child instanceof final PreferenceGroup childPreferenceGroup) {
                final PreferenceGroup copy = createPreferenceGroup(childPreferenceGroup);
                dst.addPreference(copy);
                copySrc2Dst(childPreferenceGroup, copy);
            } else {
                dst.addPreference(copy(child));
            }
        }
    }

    private static PreferenceGroup createPreferenceGroup(final PreferenceGroup childPreferenceGroup) {
        final PreferenceGroup copy = new PreferenceCategory(childPreferenceGroup.getContext());
        copy.setTitle(childPreferenceGroup.getTitle());
        return copy;
    }

    private static Preference copy(final Preference preference) {
        final Preference copy = new Preference(preference.getContext());
        copy.setKey(preference.getKey());
        copy.setIcon(preference.getIcon());
        copy.setLayoutResource(preference.getLayoutResource());
        copy.setSummary(preference.getSummary());
        copy.setTitle(preference.getTitle());
        copy.setWidgetLayoutResource(preference.getWidgetLayoutResource());
        copy.setFragment(preference.getFragment());
        copy.getExtras().putAll(preference.getExtras());
        return copy;
    }
}
