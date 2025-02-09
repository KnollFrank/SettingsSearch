package de.KnollFrank.lib.settingssearch.provider;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

public interface ActivityInitializer {

    void beforeStartActivity(PreferenceFragmentCompat src);

    Optional<Bundle> createExtras(PreferenceFragmentCompat src);
}
