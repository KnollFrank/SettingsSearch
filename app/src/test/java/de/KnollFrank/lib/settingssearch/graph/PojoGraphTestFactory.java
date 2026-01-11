package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableBiMap;

import org.jgrapht.Graph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PrincipalAndProxyProvider;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

public class PojoGraphTestFactory {

    public static Tree<PreferenceScreenWithHost, Preference> createEntityPreferenceScreenGraphRootedAt(
            final Class<? extends PreferenceFragmentCompat> root,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final Context context) {
        return createEntityPreferenceScreenGraphRootedAt(
                PojoGraphTestFactory
                        .getPreferenceScreenWithHostProvider(instantiateAndInitializeFragment)
                        .getPreferenceScreenWithHostOfFragment(
                                root,
                                Optional.empty())
                        .orElseThrow(),
                instantiateAndInitializeFragment,
                context);
    }

    public static Tree<PreferenceScreenWithHost, Preference> createEntityPreferenceScreenGraphRootedAt(
            final PreferenceScreenWithHost root,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final Context context) {
        return createEntityPreferenceScreenGraphRootedAt(
                root,
                instantiateAndInitializeFragment,
                (edge, sourceNodeOfEdge, targetNodeOfEdge) -> true,
                context);
    }

    public static Tree<PreferenceScreenWithHost, Preference> createEntityPreferenceScreenGraphRootedAt(
            final PreferenceScreenWithHost root,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final AddEdgeToGraphPredicate addEdgeToGraphPredicate,
            final Context context) {
        final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph =
                PreferenceScreenGraphProviderFactory
                        .createPreferenceScreenGraphProvider(
                                getPreferenceScreenWithHostProvider(instantiateAndInitializeFragment),
                                (preference, hostOfPreference) -> Optional.empty(),
                                classNameOfActivity -> Optional.empty(),
                                addEdgeToGraphPredicate,
                                context,
                                preferenceScreenWithHost -> {
                                })
                        .getPreferenceScreenGraph(root);
        return new Tree<>(
                GraphConverterFactory
                        .createPreferenceScreenWithHostGraphConverter()
                        .toGuava(preferenceScreenGraph));
    }

    private static PreferenceScreenWithHostProvider getPreferenceScreenWithHostProvider(final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return new PreferenceScreenWithHostProvider(
                instantiateAndInitializeFragment,
                new PrincipalAndProxyProvider(ImmutableBiMap.of()));
    }
}
