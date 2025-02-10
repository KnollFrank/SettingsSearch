package de.KnollFrank.lib.settingssearch.provider;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

public interface ActivityInitializer<T extends PreferenceFragmentCompat> {

    void beforeStartActivity(T src);

    Optional<Bundle> createExtras(T src);
}
