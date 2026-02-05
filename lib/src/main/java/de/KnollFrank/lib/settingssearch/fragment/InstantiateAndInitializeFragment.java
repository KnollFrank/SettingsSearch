package de.KnollFrank.lib.settingssearch.fragment;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.FragmentOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;

@FunctionalInterface
public interface InstantiateAndInitializeFragment {

    <T extends Fragment> FragmentOfActivity<T> instantiateAndInitializeFragment(FragmentClassOfActivity<T> fragmentClass, Optional<PreferenceOfHostOfActivity> src);
}
