package de.KnollFrank.lib.settingssearch.fragment;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;

public interface IFragments {

    <T extends Fragment> T instantiateAndInitializeFragment(Class<T> fragmentClass, Optional<PreferenceWithHost> src);
}
