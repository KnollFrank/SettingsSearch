package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Set;

public record ActivitySearchDatabaseConfigs(
        Map<Class<? extends Activity>, Class<? extends PreferenceFragmentCompat>> rootPreferenceFragmentByActivity,
        Set<PrincipalAndProxy<? extends Fragment, ? extends PreferenceFragmentCompat>> principalAndProxies) {
}
