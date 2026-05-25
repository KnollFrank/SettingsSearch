package de.KnollFrank.lib.settingssearch.search.provider;

import android.graphics.drawable.Drawable;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import com.codepoetics.ambivalence.Either;

import java.util.Optional;

@FunctionalInterface
public interface IconProvider {

    Optional<Either<Integer, Drawable>> getIconResourceIdOrIconDrawableOfPreference(Preference preference, Fragment hostOfPreference);
}
