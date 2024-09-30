package de.KnollFrank.lib.settingssearch;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.KnollFrank.lib.settingssearch.common.Preferences;

class PreferenceScreenCopier {

    private final PreferenceManager preferenceManager;

    public PreferenceScreenCopier(final PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    public PreferenceScreen copy(final PreferenceScreen preferenceScreen) {
        final PreferenceScreen copy = createCopyWithAttributes(preferenceScreen);
        copyPreferencesOfSrc2Dst(preferenceScreen, copy);
        return copy;
    }

    private void copyPreferencesOfSrc2Dst(final PreferenceGroup src, final PreferenceGroup dst) {
        for (final Preference child : Preferences.getDirectChildren(src)) {
            if (child instanceof final PreferenceGroup childPreferenceGroup) {
                final PreferenceGroup copy = createCopyWithAttributes(childPreferenceGroup);
                dst.addPreference(copy);
                copyPreferencesOfSrc2Dst(childPreferenceGroup, copy);
            } else {
                dst.addPreference(createCopyWithAttributes(child));
            }
        }
    }

    private <T extends Preference> T createCopyWithAttributes(final T preference) {
        final T copy = createInstance(preference);
        copyAttributes(preference, copy);
        return copy;
    }

    private static <T extends Preference> void copyAttributes(final T src, final T dst) {
        dst.setKey(src.getKey());
        dst.setIcon(src.getIcon());
        dst.setLayoutResource(src.getLayoutResource());
        dst.setSummary(src.getSummary());
        dst.setTitle(src.getTitle());
        dst.setWidgetLayoutResource(src.getWidgetLayoutResource());
        dst.setFragment(src.getFragment());
        dst.getExtras().putAll(src.getExtras());
    }

    private <T extends Preference> T createInstance(final T preference) {
        try {
            return _createInstance(preference);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends Preference> T _createInstance(final T preference) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        if (preference instanceof PreferenceScreen) {
            return (T) preferenceManager.createPreferenceScreen(preferenceManager.getContext());
        }
        final Class<T> clazz = (Class<T>) preference.getClass();
        final Constructor<T> constructor = clazz.getConstructor(Context.class);
        constructor.setAccessible(true);
        return constructor.newInstance(preference.getContext());
    }
}
