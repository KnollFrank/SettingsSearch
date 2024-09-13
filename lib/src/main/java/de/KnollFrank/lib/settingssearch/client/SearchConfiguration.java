package de.KnollFrank.lib.settingssearch.client;

import androidx.annotation.IdRes;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

public record SearchConfiguration(@IdRes int fragmentContainerViewId,
                                  Optional<String> queryHint,
                                  Class<? extends PreferenceFragmentCompat> rootPreferenceFragment) {
}
