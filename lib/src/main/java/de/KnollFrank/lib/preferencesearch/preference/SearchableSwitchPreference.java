package de.KnollFrank.lib.preferencesearch.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import java.util.function.Consumer;

public class SearchableSwitchPreference extends SwitchPreference implements IClickablePreference {

    private Consumer<Preference> clickListener;

    public SearchableSwitchPreference(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SearchableSwitchPreference(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SearchableSwitchPreference(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchableSwitchPreference(@NonNull final Context context) {
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
        if (getClickListener() != null) {
            getClickListener().accept(this);
        }
        super.performClick();
    }
}
