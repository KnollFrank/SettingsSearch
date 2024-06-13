package de.KnollFrank.lib.preferencesearch;

import static de.KnollFrank.lib.preferencesearch.common.PreferenceGroups.getDirectChildren;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;

import java.util.List;

class PreferenceScreensMerger {

    private final Context context;

    public PreferenceScreensMerger(final Context context) {
        this.context = context;
    }

    public PreferenceScreen destructivelyMergeScreens(final List<PreferenceScreen> screens) {
        final PreferenceScreen mergedScreens = screens.get(0).getPreferenceManager().createPreferenceScreen(context);
        for (final PreferenceScreen screen : screens) {
            destructivelyMergeScreen(screen, mergedScreens);
        }
        return mergedScreens;
    }

    private void destructivelyMergeScreen(final PreferenceScreen screen,
                                          final PreferenceScreen mergedScreens) {
        final PreferenceCategory screenCategory = createScreenCategory(screen);
        mergedScreens.addPreference(screenCategory);
        movePreferencesOfScreen2Category(screen, screenCategory);
    }

    private PreferenceCategory createScreenCategory(final PreferenceScreen screen) {
        final PreferenceCategory screenCategory = new PreferenceCategory(context);
        screenCategory.setTitle("Screen: " + screen);
        return screenCategory;
    }

    private static void movePreferencesOfScreen2Category(final PreferenceScreen screen,
                                                         final PreferenceCategory category) {
        for (final Preference preference : getDirectChildren(screen)) {
            preference.setEnabled(false);
            preference.setShouldDisableView(false);
            removePreferenceFromItsParent(preference);
            category.addPreference(preference);
        }
    }

    private static void removePreferenceFromItsParent(final Preference preference) {
        final PreferenceGroup parent = preference.getParent();
        if (parent != null) {
            parent.removePreference(preference);
        }
    }
}
