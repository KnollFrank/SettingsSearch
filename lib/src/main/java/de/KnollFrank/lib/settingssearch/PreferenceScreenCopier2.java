package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.settingssearch.common.Preferences;

class PreferenceScreenCopier2 {

    private final PreferenceManager preferenceManager;

    public PreferenceScreenCopier2(final PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    public PreferenceScreen copy(final PreferenceScreen preferenceScreen) {
        final PreferenceScreen copy = (PreferenceScreen) createCopyWithAttributes(preferenceScreen);
        copyPreferencesOfSrc2Dst(preferenceScreen, copy);
        return copy;
    }

    private void copyPreferencesOfSrc2Dst(final PreferenceGroup src, final PreferenceGroup dst) {
        for (final Preference child : Preferences.getDirectChildren(src)) {
            if (child instanceof final PreferenceGroup childPreferenceGroup) {
                final PreferenceCategory copy = new PreferenceCategory(childPreferenceGroup.getContext());
                copyAttributes(childPreferenceGroup, copy);
                dst.addPreference(copy);
                copyPreferencesOfSrc2Dst(childPreferenceGroup, copy);
            } else {
                dst.addPreference(createCopyWithAttributes(child));
            }
        }
    }

    private Preference createCopyWithAttributes(final Preference preference) {
        final Preference copy = createInstance(preference);
        copyAttributes(preference, copy);
        return copy;
    }

    private static void copyAttributes(final Preference src, final Preference dst) {
        dst.setKey(src.getKey());
        dst.setIcon(src.getIcon());
        dst.setLayoutResource(src.getLayoutResource());
        dst.setSummary(src.getSummary());
        dst.setTitle(src.getTitle());
        dst.setWidgetLayoutResource(src.getWidgetLayoutResource());
        dst.setFragment(src.getFragment());
        dst.getExtras().putAll(src.getExtras());
    }

    private Preference createInstance(final Preference preference) {
        try {
            return _createInstance(preference);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Preference _createInstance(final Preference preference) {
        if (preference instanceof PreferenceScreen) {
            return preferenceManager.createPreferenceScreen(preferenceManager.getContext());
        }
        return new Preference(preference.getContext());
    }
}
