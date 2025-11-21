package de.KnollFrank.lib.settingssearch;

import static de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst.markExtrasOfPreferenceConnectingSrcWithDst;

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
                .map(connectedFragment -> createPreferenceConnectingSrc2Dst(fragment, connectedFragment, context))
                .forEach(screen::addPreference);
        fragment.setPreferenceScreen(screen);
    }

    public static SearchablePreferenceScreen getPreferenceScreenByTitle(
            final Set<SearchablePreferenceScreen> preferenceScreens,
            final String title) {
        return preferenceScreens
                .stream()
                .filter(preferenceScreen -> preferenceScreen.title().filter(title::equals).isPresent())
                .findFirst()
                .orElseThrow();
    }

    private static Preference createPreferenceConnectingSrc2Dst(final PreferenceFragmentCompat src,
                                                                final Class<? extends Fragment> dst,
                                                                final Context context) {
        final Preference preference = new Preference(context);
        preference.setFragment(dst.getName());
        preference.setTitle("preference connected to " + dst.getSimpleName());
        preference.setKey("key of preference connected to " + dst.getSimpleName());
        markExtrasOfPreferenceConnectingSrcWithDst(preference, src, dst);
        return preference;
    }
}
