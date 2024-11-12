package de.KnollFrank.lib.settingssearch.search.provider;

import android.graphics.drawable.Drawable;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.codepoetics.ambivalence.Either;

import java.util.Optional;

@FunctionalInterface
public interface IconProvider {

    Optional<Either<Integer, Drawable>> getIconResourceIdOrIconDrawableOfPreference(Preference preference, PreferenceFragmentCompat hostOfPreference);
}
