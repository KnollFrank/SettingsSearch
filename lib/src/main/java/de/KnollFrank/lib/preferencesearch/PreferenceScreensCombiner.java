package de.KnollFrank.lib.preferencesearch;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;

import java.util.List;

import de.KnollFrank.lib.preferencesearch.common.PreferenceGroups;
import de.KnollFrank.lib.preferencesearch.results.PreferencePreparer;

class PreferenceScreensCombiner {

    private final Context context;

    public PreferenceScreensCombiner(final Context context) {
        this.context = context;
    }

    public PreferenceScreen destructivelyCombineScreens(final List<PreferenceScreen> screens) {
        final PreferenceScreen combinedScreens = screens.get(0).getPreferenceManager().createPreferenceScreen(context);
        for (final PreferenceScreen screen : screens) {
            final PreferenceCategory screenCategory = createScreenCategory(screen);
            combinedScreens.addPreference(screenCategory);
            movePreferencesOfScreen2Category(screen, screenCategory);
        }
        return combinedScreens;
    }

    private PreferenceCategory createScreenCategory(final PreferenceScreen screen) {
        final PreferenceCategory screenCategory = new PreferenceCategory(context);
        screenCategory.setTitle("Screen: " + screen);
        return screenCategory;
    }

    private static void movePreferencesOfScreen2Category(final PreferenceScreen screen,
                                                         final PreferenceCategory category) {
        for (final Preference preference : PreferenceGroups.getImmediateChildrenOf(screen)) {
            PreferencePreparer.removePreferenceFromItsParent(preference);
            category.addPreference(preference);
        }
    }
}
