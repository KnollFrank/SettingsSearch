package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;
import android.content.Intent;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.common.Utils;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public class PreferencePathNavigator {

    public static final String PREFERENCE_PATH = "preferencePath";
    public static final String INDEX_WITHIN_PREFERENCE_PATH = "indexWithinPreferencePath";

    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    private final Context context;

    public PreferencePathNavigator(final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                   final Context context) {
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.context = context;
    }

    public Optional<PreferenceFragmentCompat> navigatePreferencePath(final PreferencePath preferencePath) {
        return navigatePreferences(preferencePath.preferences(), Optional.empty(), 0);
    }

    private Optional<PreferenceFragmentCompat> navigatePreferences(final List<SearchablePreference> preferences,
                                                                   final Optional<PreferenceWithHost> src,
                                                                   final int indexWithinPreferencePath) {
        if (preferences.isEmpty()) {
            return Optional.of(src.orElseThrow().host());
        }
        final SearchablePreference preference = Lists.head(preferences);
        if (preference.getClassNameOfReferencedActivity().isPresent()) {
            continueNavigationInAnotherActivity(
                    preference.getClassNameOfReferencedActivity().get(),
                    Lists.tail(preferences),
                    indexWithinPreferencePath + 1);
            return Optional.empty();
        }
        return navigatePreferences(
                Lists.tail(preferences),
                Optional.of(getPreferenceWithHost(preference, src)),
                indexWithinPreferencePath + 1);
    }

    private void continueNavigationInAnotherActivity(final String classNameOfReferencedActivity,
                                                     final List<SearchablePreference> preferences,
                                                     final int indexWithinPreferencePath) {
        context.startActivity(
                createIntent(
                        classNameOfReferencedActivity,
                        new PreferencePath(preferences),
                        indexWithinPreferencePath));
    }

    private Intent createIntent(final String classNameOfReferencedActivity,
                                final PreferencePath preferencePath,
                                final int indexWithinPreferencePath) {
        final Intent intent =
                new Intent(
                        context,
                        Utils.getClass(classNameOfReferencedActivity));
        // FK-TODO: introduce record for PREFERENCE_PATH and INDEX_WITHIN_PREFERENCE_PATH and make this two constants private
        intent.putIntegerArrayListExtra(PREFERENCE_PATH, new ArrayList<>(getPreferencePathIds(preferencePath)));
        intent.putExtra(INDEX_WITHIN_PREFERENCE_PATH, indexWithinPreferencePath);
        return intent;
    }

    private static List<Integer> getPreferencePathIds(final PreferencePath preferencePath) {
        return preferencePath
                .preferences()
                .stream()
                .map(SearchablePreference::getId)
                .collect(Collectors.toList());
    }

    private PreferenceWithHost getPreferenceWithHost(final SearchablePreference preference,
                                                     final Optional<PreferenceWithHost> src) {
        final PreferenceFragmentCompat hostOfPreference = instantiateAndInitializePreferenceFragment(preference.getHost(), src);
        return new PreferenceWithHost(
                getPreference(hostOfPreference, preference.getKey().orElseThrow()),
                hostOfPreference);
    }

    private PreferenceFragmentCompat instantiateAndInitializePreferenceFragment(
            final Class<? extends PreferenceFragmentCompat> preferenceFragment,
            final Optional<PreferenceWithHost> src) {
        return (PreferenceFragmentCompat) fragmentFactoryAndInitializer.instantiateAndInitializeFragment(
                preferenceFragment.getName(),
                src,
                context);
    }

    private static Preference getPreference(final PreferenceFragmentCompat hostOfPreference,
                                            final String keyOfPreference) {
        final Preference preference = hostOfPreference.findPreference(keyOfPreference);
        if (preference == null) {
            throw new IllegalArgumentException("can not find preference with key " + keyOfPreference + " within preferenceFragment " + hostOfPreference);
        }
        return preference;
    }
}
