package de.KnollFrank.lib.settingssearch.search.provider;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import java.util.Optional;

@FunctionalInterface
public interface IconResourceIdProvider {

    @IdRes
    Optional<Integer> getIconResourceIdOfPreference(Preference preference, Fragment hostOfPreference);
}
