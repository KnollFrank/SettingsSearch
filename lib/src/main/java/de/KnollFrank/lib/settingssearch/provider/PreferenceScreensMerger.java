package de.KnollFrank.lib.settingssearch.provider;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Preferences;

// FK-TODO: remove?
public class PreferenceScreensMerger {

    private final Context context;

    public record PreferenceScreenAndNonClickablePreferences(PreferenceScreen preferenceScreen,
                                                             Set<PreferenceCategory> nonClickablePreferences) {
    }

    public PreferenceScreensMerger(final Context context) {
        this.context = context;
    }

    public PreferenceScreenAndNonClickablePreferences destructivelyMergeScreens(final List<PreferenceScreen> screens) {
        final PreferenceScreen mergedScreens = screens.get(0).getPreferenceManager().createPreferenceScreen(context);
        final ImmutableSet.Builder<PreferenceCategory> nonClickablePreferences = ImmutableSet.builder();
        for (final PreferenceScreen screen : screens) {
            destructivelyMergeSrcIntoDst(screen, mergedScreens, nonClickablePreferences);
        }
        return new PreferenceScreenAndNonClickablePreferences(mergedScreens, nonClickablePreferences.build());
    }

    private void destructivelyMergeSrcIntoDst(final PreferenceScreen src,
                                              final PreferenceScreen dst,
                                              final ImmutableSet.Builder<PreferenceCategory> nonClickablePreferences) {
        final PreferenceCategory screenCategory = createScreenCategory(src.toString());
        dst.addPreference(screenCategory);
        moveChildrenOfSrc2Dst(src, screenCategory);
        nonClickablePreferences.add(screenCategory);
    }

    private PreferenceCategory createScreenCategory(final String title) {
        final PreferenceCategory screenCategory = new PreferenceCategory(context);
        screenCategory.setTitle(title);
        return screenCategory;
    }

    private static void moveChildrenOfSrc2Dst(final PreferenceGroup src, final PreferenceGroup dst) {
        Preferences
                .getImmediateChildren(src)
                .forEach(child -> movePreference2PreferenceGroup(child, dst));
    }

    private static void movePreference2PreferenceGroup(final Preference preference,
                                                       final PreferenceGroup preferenceGroup) {
        preservePreferenceDataStore(preference);
        removePreferenceFromItsParent(preference);
        preferenceGroup.addPreference(preference);
    }

    private static void preservePreferenceDataStore(final Preference preference) {
        preference.setPreferenceDataStore(preference.getPreferenceDataStore());
    }

    private static void removePreferenceFromItsParent(final Preference preference) {
        final PreferenceGroup parent = preference.getParent();
        if (parent != null) {
            parent.removePreference(preference);
        }
    }
}
