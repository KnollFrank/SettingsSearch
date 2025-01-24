package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;
import android.content.Intent;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.common.Utils;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public class PreferencePathNavigator {

    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    private final Context context;

    public PreferencePathNavigator(final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                   final Context context) {
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.context = context;
    }

    public Optional<PreferenceFragmentCompat> navigatePreferencePath(final PreferencePath preferencePath,
                                                                     final int startNavigationAtIndexWithinPreferencePath) {
        return navigatePreferences(
                Optional.empty(),
                preferencePath,
                startNavigationAtIndexWithinPreferencePath);
    }

    private Optional<PreferenceFragmentCompat> navigatePreferences(
            final Optional<PreferenceWithHost> src,
            final PreferencePath preferencePath,
            final int indexWithinPreferencePath) {
        final Optional<SearchablePreference> preferenceOption =
                Lists.getElementAtIndex(
                        preferencePath.preferences(),
                        indexWithinPreferencePath);
        if (preferenceOption.isEmpty()) {
            return Optional.of(src.orElseThrow().host());
        }
        final SearchablePreference preference = preferenceOption.get();
        if (preference.getClassNameOfReferencedActivity().isPresent()) {
            continueNavigationInAnotherActivity(
                    preference.getClassNameOfReferencedActivity().get(),
                    preferencePath,
                    indexWithinPreferencePath + 1);
            return Optional.empty();
        }
        return navigatePreferences(
                Optional.of(getPreferenceWithHost(preference, src)),
                preferencePath,
                indexWithinPreferencePath + 1);
    }

    private void continueNavigationInAnotherActivity(final String classNameOfReferencedActivity,
                                                     final PreferencePath preferencePath,
                                                     final int indexWithinPreferencePath) {
        context.startActivity(
                createIntent(
                        classNameOfReferencedActivity,
                        preferencePath,
                        indexWithinPreferencePath));
    }

    private Intent createIntent(final String classNameOfReferencedActivity,
                                final PreferencePath preferencePath,
                                final int indexWithinPreferencePath) {
        final Intent intent =
                new Intent(
                        context,
                        Utils.getClass(classNameOfReferencedActivity));
        final PreferencePathNavigatorData preferencePathNavigatorData =
                new PreferencePathNavigatorData(
                        preferencePath.getPreference().getId(),
                        indexWithinPreferencePath);
        intent.putExtras(preferencePathNavigatorData.toBundle());
        return intent;
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
