package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

public record ActivitySearchDatabaseConfig<A extends Activity, F extends Fragment, P1 extends PreferenceFragmentCompat, P2 extends PreferenceFragmentCompat>(
        ActivityWithRootPreferenceFragment<A, P1> activityWithRootPreferenceFragment,
        Optional<FragmentWithPreferenceFragmentConnection_PreferenceFragmentInitializer<F, P2>> fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer) {
}
