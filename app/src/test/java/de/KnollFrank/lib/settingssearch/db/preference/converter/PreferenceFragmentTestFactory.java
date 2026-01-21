package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static de.KnollFrank.lib.settingssearch.InstantiateAndInitializeFragmentFactory.createInstantiateAndInitializeFragment;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenToSearchablePreferenceScreenConverterTest.initializeFragment;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.db.preference.dao.TestPreferenceFragment;

public class PreferenceFragmentTestFactory {

    private PreferenceFragmentTestFactory() {
    }

    public static PreferenceFragmentCompat createSomePreferenceFragment(final FragmentActivity activity) {
        final PreferenceFragmentCompat preferenceFragment = new TestPreferenceFragment();
        return (PreferenceFragmentCompat) initializeFragment(
                preferenceFragment,
                createInstantiateAndInitializeFragment(preferenceFragment, activity));
    }
}
