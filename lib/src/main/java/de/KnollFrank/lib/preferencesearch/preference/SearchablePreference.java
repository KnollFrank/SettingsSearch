package de.KnollFrank.lib.preferencesearch.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;

import java.util.function.Consumer;

public class SearchablePreference extends Preference implements IClickablePreference {

    private Consumer<Preference> clickListener = preference -> {
    };

    public SearchablePreference(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SearchablePreference(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SearchablePreference(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchablePreference(@NonNull final Context context) {
        super(context);
    }

    @Override
    public void setClickListener(final Consumer<Preference> clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void performClick() {
        clickListener.accept(this);
        super.performClick();
    }
}
