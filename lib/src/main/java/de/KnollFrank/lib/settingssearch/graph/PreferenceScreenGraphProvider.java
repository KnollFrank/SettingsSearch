package de.KnollFrank.lib.settingssearch.graph;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.Classes;
import de.KnollFrank.lib.settingssearch.common.Intents;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;

public class PreferenceScreenGraphProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider;
    private final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider;
    private final PreferenceScreenGraphListener preferenceScreenGraphListener;
    private final Context context;
    private Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph;

    public PreferenceScreenGraphProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                         final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
                                         final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider,
                                         final PreferenceScreenGraphListener preferenceScreenGraphListener,
                                         final Context context) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceFragmentConnected2PreferenceProvider = preferenceFragmentConnected2PreferenceProvider;
        this.rootPreferenceFragmentOfActivityProvider = rootPreferenceFragmentOfActivityProvider;
        this.preferenceScreenGraphListener = preferenceScreenGraphListener;
        this.context = context;
    }

    public Graph<PreferenceScreenWithHost, PreferenceEdge> getPreferenceScreenGraph(final Class<? extends Fragment> rootPreferenceFragmentClass) {
        preferenceScreenGraph = PreferenceScreenGraphFactory.createEmptyPreferenceScreenGraph(preferenceScreenGraphListener);
        buildPreferenceScreenGraph(
                preferenceScreenWithHostProvider
                        .getPreferenceScreenWithHostOfFragment(
                                rootPreferenceFragmentClass,
                                Optional.empty())
                        .orElseThrow());
        return preferenceScreenGraph;
    }

    private void buildPreferenceScreenGraph(final PreferenceScreenWithHost root) {
        if (preferenceScreenGraph.containsVertex(root)) {
            return;
        }
        preferenceScreenGraph.addVertex(root);
        // FK-TODO: remove sleep
//        try {
//            Thread.sleep(1000);
//        } catch (final InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        this
                .getConnectedPreferenceScreenByPreference(root)
                .forEach(
                        (preference, child) -> {
                            buildPreferenceScreenGraph(child);
                            preferenceScreenGraph.addVertex(child);
                            preferenceScreenGraph.addEdge(root, child, new PreferenceEdge(preference));
                        });
    }

    private Map<Preference, PreferenceScreenWithHost> getConnectedPreferenceScreenByPreference(final PreferenceScreenWithHost preferenceScreenWithHost) {
        return Maps.filterPresentValues(
                Preferences
                        .getChildrenRecursively(preferenceScreenWithHost.preferenceScreen())
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        Function.identity(),
                                        preference -> getConnectedPreferenceScreen(preference, preferenceScreenWithHost.host()))));
    }

    private Optional<PreferenceScreenWithHost> getConnectedPreferenceScreen(
            final Preference preference,
            final PreferenceFragmentCompat host) {
        return this
                .getConnectedPreferenceFragment(preference, host)
                .flatMap(
                        fragmentConnectedToPreference ->
                                preferenceScreenWithHostProvider
                                        .getPreferenceScreenWithHostOfFragment(
                                                fragmentConnectedToPreference,
                                                Optional.of(new PreferenceWithHost(preference, host))));
    }

    private Optional<Class<? extends Fragment>> getConnectedPreferenceFragment(final Preference preference, final PreferenceFragmentCompat host) {
        return PreferenceScreenGraphProvider
                .loadFragmentClass(preference, context)
                .or(() -> getRootPreferenceFragment(Optional.ofNullable(preference.getIntent())))
                .or(() -> preferenceFragmentConnected2PreferenceProvider.getPreferenceFragmentConnected2Preference(preference, host));
    }

    private static Optional<Class<? extends Fragment>> loadFragmentClass(final Preference preference, final Context context) {
        return Optional
                .ofNullable(preference.getFragment())
                .map(fragmentClassName -> Classes.loadFragmentClass(fragmentClassName, context));
    }

    private Optional<Class<? extends PreferenceFragmentCompat>> getRootPreferenceFragment(final Optional<Intent> intent) {
        return intent
                .map(Intents::getClassName)
                .flatMap(this::asActivityClass)
                .flatMap(rootPreferenceFragmentOfActivityProvider::getRootPreferenceFragmentOfActivity);
    }

    private Optional<Class<? extends Activity>> asActivityClass(final String className) {
        final Class<?> clazz = Classes.loadClass(className, context);
        return Activity.class.isAssignableFrom(clazz) ?
                Optional.of((Class<? extends Activity>) clazz) :
                Optional.empty();
    }
}
