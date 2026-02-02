package de.KnollFrank.lib.settingssearch;

import android.os.PersistableBundle;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.ActivityDescription;
import de.KnollFrank.settingssearch.test.TestActivity;

public class PreferenceFragmentClassOfActivityTestFactory {

    private PreferenceFragmentClassOfActivityTestFactory() {
    }

    public static PreferenceFragmentClassOfActivity createSomePreferenceFragmentClassOfActivity() {
        return new PreferenceFragmentClassOfActivity(
                PreferenceFragmentCompat.class,
                new ActivityDescription(
                        TestActivity.class,
                        new PersistableBundle()));
    }
}
