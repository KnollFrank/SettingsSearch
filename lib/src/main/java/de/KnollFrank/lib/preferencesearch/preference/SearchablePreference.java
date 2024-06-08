package de.KnollFrank.lib.preferencesearch.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.function.Consumer;

import de.KnollFrank.lib.preferencesearch.PreferenceWithHost;

// FK-TODO: verwende https://github.com/raphw/byte-buddy/tree/master/byte-buddy-gradle-plugin oder https://github.com/linkedin/dexmaker um die Searchable*-Klassen anders zu erzeugen und in den preferences.xml-Dateien wieder die Original-Preferences (aus dem package androidx.preference) verwenden zu können.
public class SearchablePreference extends Preference implements IClickablePreference {

    private Consumer<PreferenceWithHost> preferenceClickListener = preferenceWithHost -> {
    };
    private Class<? extends PreferenceFragmentCompat> host;

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
