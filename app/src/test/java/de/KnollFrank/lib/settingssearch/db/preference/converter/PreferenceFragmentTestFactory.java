package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHost2POJOConverterTest.getInstantiateAndInitializeFragment;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHost2POJOConverterTest.initializeFragment;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.db.preference.dao.TestPreferenceFragment;
import de.KnollFrank.settingssearch.test.TestActivity;

public class PreferenceFragmentTestFactory {

    public static PreferenceFragmentCompat createSomePreferenceFragment(final TestActivity activity) {
        final PreferenceFragmentCompat preferenceFragment = new TestPreferenceFragment();
        return (PreferenceFragmentCompat) initializeFragment(
                preferenceFragment,
                getInstantiateAndInitializeFragment(preferenceFragment, activity));
    }
}
