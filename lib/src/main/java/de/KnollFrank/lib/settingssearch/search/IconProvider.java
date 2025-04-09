package de.KnollFrank.lib.settingssearch.search;

import android.graphics.drawable.Drawable;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.codepoetics.ambivalence.Either;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.search.provider.IconResourceIdProvider;

class IconProvider implements de.KnollFrank.lib.settingssearch.search.provider.IconProvider {

    private final IconResourceIdProvider iconResourceIdProvider;

    public IconProvider(final IconResourceIdProvider iconResourceIdProvider) {
        this.iconResourceIdProvider = iconResourceIdProvider;
    }

    @Override
    public Optional<Either<Integer, Drawable>> getIconResourceIdOrIconDrawableOfPreference(final Preference preference, final PreferenceFragmentCompat hostOfPreference) {
        return this
                .getIconResourceId(preference, hostOfPreference)
                .or(() -> getIconDrawable(preference));
    }

    private Optional<Either<Integer, Drawable>> getIconResourceId(final Preference preference,
                                                                  final PreferenceFragmentCompat hostOfPreference) {
        return iconResourceIdProvider
                .getIconResourceIdOfPreference(preference, hostOfPreference)
                .map(Either::ofLeft);
    }

    public static Optional<Either<Integer, Drawable>> getIconDrawable(final Preference preference) {
        return Optional
                .ofNullable(preference.getIcon())
                .map(Either::ofRight);
    }
}
