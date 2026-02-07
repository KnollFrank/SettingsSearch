package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import android.app.Activity;

import androidx.preference.PreferenceFragmentCompat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import de.KnollFrank.lib.settingssearch.ActivityDescription;
import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;

@RunWith(RobolectricTestRunner.class)
public class PreferenceFragmentClassOfActivityConverterTest {

    @Test
    public void shouldConvertFragmentClassOfActivityToStringAndBack() {
        ConverterTest.test_a_convertForward_convertBackward(
                createSomeFragmentClassOfActivity(),
                new PreferenceFragmentClassOfActivityConverter(),
                FragmentClassOfActivityMatchers::isEqualTo);
    }

    private static FragmentClassOfActivity<? extends PreferenceFragmentCompat> createSomeFragmentClassOfActivity() {
        return new FragmentClassOfActivity<>(
                TestPreferenceFragment.class,
                new ActivityDescription(
                        TestActivity.class,
                        PersistableBundleTestFactory.createSomePersistableBundle()));
    }

    public static class TestPreferenceFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final android.os.Bundle savedInstanceState, final String rootKey) {
        }
    }

    public static class TestActivity extends Activity {
    }
}
