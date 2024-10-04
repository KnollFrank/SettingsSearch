package de.KnollFrank.lib.settingssearch;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.Set;

class PreferenceScreensProviderTestHelper {

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

    public static PreferenceScreenWithHostClass getPreferenceScreenByName(
            final Set<PreferenceScreenWithHostClass> preferenceScreens,
            final String name) {
        return preferenceScreens
                .stream()
                .filter(preferenceScreenWithHostClass -> name.equals(preferenceScreenWithHostClass.preferenceScreen().toString()))
                .findFirst()
                .get();
    }

    private static Preference createConnectionToFragment(final Class<? extends Fragment> fragment,
                                                         final Context context) {
        final Preference preference = new Preference(context);
        preference.setFragment(fragment.getName());
        preference.setTitle("preference connected to " + fragment.getSimpleName());
        return preference;
    }
}
