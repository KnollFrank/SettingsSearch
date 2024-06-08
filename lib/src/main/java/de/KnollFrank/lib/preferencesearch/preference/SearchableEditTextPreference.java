package de.KnollFrank.lib.preferencesearch.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.function.Consumer;

import de.KnollFrank.lib.preferencesearch.PreferenceWithHost;

public class SearchableEditTextPreference extends EditTextPreference implements IClickablePreference {

    private Consumer<PreferenceWithHost> preferenceClickListener = preferenceWithHost -> {
    };
    private Class<? extends PreferenceFragmentCompat> host;

    public SearchableEditTextPreference(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SearchableEditTextPreference(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SearchableEditTextPreference(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchableEditTextPreference(@NonNull final Context context) {
        super(context);
    }

    @Override
    public void setPreferenceClickListenerAndHost(final Consumer<PreferenceWithHost> preferenceClickListener,
                                                  final Class<? extends PreferenceFragmentCompat> host) {
        this.preferenceClickListener = preferenceClickListener;
        this.host = host;
    }

    @Override
    public void performClick() {
        preferenceClickListener.accept(new PreferenceWithHost(this, host));
        super.performClick();
    }
}
