package de.KnollFrank.lib.settingssearch.db.preference;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceCategory;

import com.codepoetics.ambivalence.Either;

import java.util.Optional;

public class SearchablePreference extends PreferenceCategory {

    private final Optional<String> searchableInfo;
    private final Optional<Either<Integer, Drawable>> iconResourceIdOrIconDrawable;

    public SearchablePreference(@NonNull final Context context,
                                @Nullable final AttributeSet attrs,
                                final Optional<String> searchableInfo,
                                final Optional<Either<Integer, Drawable>> iconResourceIdOrIconDrawable) {
        super(context, attrs);
        this.searchableInfo = searchableInfo;
        this.iconResourceIdOrIconDrawable = iconResourceIdOrIconDrawable;
        setIcon(iconResourceIdOrIconDrawable);
    }

    public SearchablePreference(@NonNull final Context context,
                                final Optional<String> searchableInfo,
                                final Optional<Either<Integer, Drawable>> iconResourceIdOrIconDrawable) {
        this(context, null, searchableInfo, iconResourceIdOrIconDrawable);
    }

    public Optional<String> getSearchableInfo() {
        return searchableInfo;
    }

    @Override
    public void setIcon(final int iconResId) {
        // constant icon already set in constructor
    }

    @Override
    public void setIcon(@Nullable final Drawable icon) {
        // constant icon already set in constructor
    }

    @Override
    public String toString() {
        return "SearchablePreference{" +
                "searchableInfo=" + searchableInfo +
                ", iconResourceIdOrIconDrawable=" + iconResourceIdOrIconDrawable +
                "} " + super.toString();
    }

    private void setIcon(final Optional<Either<Integer, Drawable>> iconResourceIdOrIconDrawable) {
        iconResourceIdOrIconDrawable.ifPresent(
                _iconResourceIdOrIconDrawable ->
                        _iconResourceIdOrIconDrawable.forEither(
                                super::setIcon,
                                super::setIcon));
    }
}
