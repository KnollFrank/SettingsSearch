package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

@FunctionalInterface
public interface FragmentFactory {

    // instantiate fragmentClass, where fragmentClass.fragment().getName().equals(src.orElseThrow().preference.getFragment()) if src.isPresent()
    <T extends Fragment> T instantiate(FragmentClassOfActivity<T> fragmentClass,
                                       Optional<PreferenceOfHostOfActivity> src,
                                       Context context,
                                       InstantiateAndInitializeFragment instantiateAndInitializeFragment);
}
