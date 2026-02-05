package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.FragmentOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.common.Classes;

public class DefaultFragmentFactory implements FragmentFactory {

    @Override
    public <T extends Fragment> FragmentOfActivity<T> instantiate(
            final FragmentClassOfActivity<T> fragmentClass,
            final Optional<PreferenceOfHostOfActivity> src,
            final Context context,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return new FragmentOfActivity<>(
                Classes.instantiateFragmentClass(
                        fragmentClass.fragment(),
                        peekExtrasOfPreference(src)),
                fragmentClass.activityOFragment());
    }

    private static Optional<Bundle> peekExtrasOfPreference(final Optional<PreferenceOfHostOfActivity> preference) {
        return preference
                .map(PreferenceOfHostOfActivity::preference)
                .map(Preference::peekExtras);
    }
}
