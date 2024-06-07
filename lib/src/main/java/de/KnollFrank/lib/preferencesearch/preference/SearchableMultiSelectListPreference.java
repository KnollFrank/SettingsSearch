package de.KnollFrank.lib.preferencesearch.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;

import java.util.function.Consumer;

public class SearchableMultiSelectListPreference extends MultiSelectListPreference implements IClickablePreference {

    private Consumer<Preference> clickListener = preference -> {
    };

    public SearchableMultiSelectListPreference(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SearchableMultiSelectListPreference(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SearchableMultiSelectListPreference(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchableMultiSelectListPreference(@NonNull final Context context) {
        super(context);
    }

    @Override
    public void setClickListener(final Consumer<Preference> clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public Consumer<Preference> getClickListener() {
        return clickListener;
    }

    @Override
    public void performClick() {
        getClickListener().accept(this);
        super.performClick();
    }
}
