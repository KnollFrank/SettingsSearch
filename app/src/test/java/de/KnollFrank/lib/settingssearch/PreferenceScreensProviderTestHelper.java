package de.KnollFrank.lib.settingssearch;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

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

    public static SearchablePreferenceScreen getPreferenceScreenByName(
            final Set<SearchablePreferenceScreen> preferenceScreens,
            final String name) {
        return preferenceScreens
                .stream()
                .filter(preferenceScreen -> preferenceScreen.getTitle().filter(name::equals).isPresent())
                .findFirst()
                .orElseThrow();
    }

    private static Preference createConnectionToFragment(final Class<? extends Fragment> fragment,
                                                         final Context context) {
        final Preference preference = new Preference(context);
        preference.setFragment(fragment.getName());
        preference.setTitle("preference connected to " + fragment.getSimpleName());
        preference.setKey("key of preference connected to " + fragment.getSimpleName());
        return preference;
    }
}
