package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.graph.ImmutableValueGraph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PrincipalAndProxyProvider;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class PojoTreeTestFactory {

    private PojoTreeTestFactory() {
    }

    public static Tree<PreferenceScreenOfHostOfActivity, Preference, ImmutableValueGraph<PreferenceScreenOfHostOfActivity, Preference>> createEntityPreferenceScreenTreeRootedAt(
            final FragmentClassOfActivity<? extends PreferenceFragmentCompat> root,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final Context context) {
        return createEntityPreferenceScreenTreeRootedAt(
                PojoTreeTestFactory
                        .getPreferenceScreenWithHostProvider(instantiateAndInitializeFragment)
                        .getPreferenceScreenWithHostOfFragment(
                                root,
                                Optional.empty())
                        .orElseThrow(),
                instantiateAndInitializeFragment,
                context);
    }

    public static Tree<PreferenceScreenOfHostOfActivity, Preference, ImmutableValueGraph<PreferenceScreenOfHostOfActivity, Preference>> createEntityPreferenceScreenTreeRootedAt(
            final PreferenceScreenOfHostOfActivity root,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final Context context) {
        return createEntityPreferenceScreenTreeRootedAt(
                root,
                instantiateAndInitializeFragment,
                edge -> true,
                context);
    }

    public static Tree<PreferenceScreenOfHostOfActivity, Preference, ImmutableValueGraph<PreferenceScreenOfHostOfActivity, Preference>> createEntityPreferenceScreenTreeRootedAt(
            final PreferenceScreenOfHostOfActivity root,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final AddEdgeToTreePredicate<PreferenceScreenOfHostOfActivity, Preference> addEdgeToTreePredicate,
            final Context context) {
        return PreferenceScreenTreeBuilderFactory
                .createPreferenceScreenTreeBuilder(
                        getPreferenceScreenWithHostProvider(instantiateAndInitializeFragment),
                        (preference, hostOfPreference) -> Optional.empty(),
                        classNameOfActivity -> Optional.empty(),
                        addEdgeToTreePredicate,
                        TreeBuilderListeners.emptyTreeBuilderListener(),
                        context)
                .buildTreeWithRoot(root);
    }

    private static PreferenceScreenWithHostProvider getPreferenceScreenWithHostProvider(final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return new PreferenceScreenWithHostProvider(
                instantiateAndInitializeFragment,
                new PrincipalAndProxyProvider(ImmutableBiMap.of()));
    }
}
