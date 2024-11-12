package de.KnollFrank.lib.settingssearch.search;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.search.provider.IconResourceIdProvider;

public class ReflectionIconResourceIdProvider implements IconResourceIdProvider {

    @Override
    public Optional<Integer> getIconResourceIdOfPreference(final Preference preference, final PreferenceFragmentCompat hostOfPreference) {
        final int iconResourceId = getIconResourceId(preference);
        return iconResourceId != 0 ? Optional.of(iconResourceId) : Optional.empty();
    }

    private static int getIconResourceId(final Preference preference) {
        try {
            return (int) FieldUtils.readField(preference, "mIconResId", true);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
