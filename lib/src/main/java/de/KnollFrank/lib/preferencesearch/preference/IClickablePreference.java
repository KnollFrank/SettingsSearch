package de.KnollFrank.lib.preferencesearch.preference;

import androidx.preference.PreferenceFragmentCompat;

import java.util.function.Consumer;

import de.KnollFrank.lib.preferencesearch.PreferenceWithHost;

public interface IClickablePreference {

    void setPreferenceClickListenerAndHost(Consumer<PreferenceWithHost> preferenceClickListener, Class<? extends PreferenceFragmentCompat> host);
}
