package com.bytehamster.lib.preferencesearch;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.List;

class PreferencesGraphProviderTestHelper {

    public static void configureConnectedPreferencesOfFragment(
            final PreferenceFragmentCompat fragment,
            final String title,
            final List<Class<? extends Fragment>> connectedFragments) {
        final Context context = fragment.getPreferenceManager().getContext();
        final PreferenceScreen screen = fragment.getPreferenceManager().createPreferenceScreen(context);
        screen.setTitle(title);
        connectedFragments
                .stream()
                .map(connectedFragment -> createConnectionToFragment(connectedFragment, context))
                .forEach(screen::addPreference);
        fragment.setPreferenceScreen(screen);
    }

    public static PreferenceScreenWithHost getPreferenceScreenByName(final Graph<PreferenceScreenWithHost, DefaultEdge> preferencesGraph,
                                                                     final String name) {
        return preferencesGraph
                .vertexSet()
                .stream()
                .filter(preferenceScreen -> name.equals(preferenceScreen.preferenceScreen.toString()))
                .findFirst()
                .get();
    }

    private static Preference createConnectionToFragment(final Class<? extends Fragment> fragment,
                                                         final Context context) {
        final Preference preference = new Preference(context);
        preference.setFragment(fragment.getName());
        return preference;
    }
}
