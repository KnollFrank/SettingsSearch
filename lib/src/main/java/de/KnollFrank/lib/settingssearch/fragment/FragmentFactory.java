package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.FragmentOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;

@FunctionalInterface
public interface FragmentFactory {

    // instantiate fragmentClass, where fragmentClass.fragment().getName().equals(src.orElseThrow().preference.getFragment()) if src.isPresent()
    // FK-TODO: für SearchDatabaseConfigBuilder.fragmentFactory sollte es eine FragmentFactory geben mit Rückgabetyp T statt FragmentOfActivity<T>
    <T extends Fragment> FragmentOfActivity<T> instantiate(
            FragmentClassOfActivity<T> fragmentClass,
            Optional<PreferenceOfHostOfActivity> src,
            Context context,
            InstantiateAndInitializeFragment instantiateAndInitializeFragment);
}
