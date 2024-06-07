package de.KnollFrank.lib.preferencesearch.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;

import java.util.function.Consumer;

public class SearchableCheckBoxPreference extends CheckBoxPreference implements IClickablePreference {

    private Consumer<Preference> clickListener = preference -> {
    };

    public SearchableCheckBoxPreference(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SearchableCheckBoxPreference(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SearchableCheckBoxPreference(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchableCheckBoxPreference(@NonNull final Context context) {
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

