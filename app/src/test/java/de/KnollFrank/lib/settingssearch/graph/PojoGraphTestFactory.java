package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

public class PojoGraphTestFactory {

    public static Graph<PreferenceScreenWithHost, PreferenceEdge> createSomeEntityPreferenceScreenGraph(
            final PreferenceFragmentCompat preferenceFragment,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final Context context) {
        final PreferenceScreenGraphProvider preferenceScreenGraphProvider =
                new PreferenceScreenGraphProvider(
                        new PreferenceScreenWithHostProvider(
                                instantiateAndInitializeFragment,
                                PreferenceFragmentCompat::getPreferenceScreen,
                                fragment -> Optional.empty()),
                        (preference, hostOfPreference) -> Optional.empty(),
                        classNameOfActivity -> Optional.empty(),
                        preferenceScreenWithHost -> {
                        },
                        context);
        return preferenceScreenGraphProvider.getPreferenceScreenGraph(preferenceFragment.getClass());
    }
}
