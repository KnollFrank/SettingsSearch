package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreen2SearchablePreferenceEntityScreenEntityConverterTest.getInstantiateAndInitializeFragment;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreen2SearchablePreferenceEntityScreenEntityConverterTest.initializeFragment;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.db.preference.dao.TestPreferenceFragment;

public class PreferenceFragmentTestFactory {

    public static PreferenceFragmentCompat createSomePreferenceFragment(final FragmentActivity activity) {
        final PreferenceFragmentCompat preferenceFragment = new TestPreferenceFragment();
        return (PreferenceFragmentCompat) initializeFragment(
                preferenceFragment,
                getInstantiateAndInitializeFragment(preferenceFragment, activity));
    }
}
