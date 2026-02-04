package de.KnollFrank.lib.settingssearch.fragment;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;

@FunctionalInterface
public interface InstantiateAndInitializeFragment {

    // FK-TODO: introduce class FragmentOfActivity (analogous to PreferenceFragmentOfActivity) as return type
    <T extends Fragment> T instantiateAndInitializeFragment(FragmentClassOfActivity<T> fragmentClass, Optional<PreferenceOfHostOfActivity> src);
}
