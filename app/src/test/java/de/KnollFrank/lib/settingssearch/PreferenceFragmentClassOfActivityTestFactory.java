package de.KnollFrank.lib.settingssearch;

import android.os.PersistableBundle;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.settingssearch.test.TestActivity;

public class PreferenceFragmentClassOfActivityTestFactory {

    private PreferenceFragmentClassOfActivityTestFactory() {
    }

    public static FragmentClassOfActivity<PreferenceFragmentCompat> createSomePreferenceFragmentClassOfActivity() {
        return new FragmentClassOfActivity<>(
                PreferenceFragmentCompat.class,
                new ActivityDescription(
                        TestActivity.class,
                        new PersistableBundle()));
    }
}
