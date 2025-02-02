package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;
import android.content.Intent;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.Utils;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public class PreferencePathNavigator {

    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    private final Context context;
    private final Fragments fragments;

    public PreferencePathNavigator(final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                   final Context context,
                                   final Fragments fragments) {
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.context = context;
        this.fragments = fragments;
    }

    public Optional<PreferenceFragmentCompat> navigatePreferencePath(final PreferencePathPointer preferencePathPointer) {
        return navigatePreferences(preferencePathPointer, Optional.empty());
    }

    private Optional<PreferenceFragmentCompat> navigatePreferences(final PreferencePathPointer preferencePathPointer,
                                                                   final Optional<PreferenceWithHost> src) {
        final Optional<String> classNameOfActivity = preferencePathPointer.dereference().getClassNameOfReferencedActivity();
        return classNameOfActivity.isPresent() ?
                continueNavigationInActivity(
                        classNameOfActivity.orElseThrow(),
                        preferencePathPointer,
                        src) :
                navigatePreferences(
                        preferencePathPointer.next(),
                        getPreferenceWithHost(preferencePathPointer.dereference(), src));
    }

    private Optional<PreferenceFragmentCompat> navigatePreferences(final Optional<PreferencePathPointer> preferencePathPointer,
                                                                   final PreferenceWithHost src) {
        return preferencePathPointer.isEmpty() ?
                Optional.of(src.host()) :
                navigatePreferences(preferencePathPointer.get(), Optional.of(src));
    }

    private Optional<PreferenceFragmentCompat> continueNavigationInActivity(final String classNameOfActivity,
                                                                            final PreferencePathPointer preferencePathPointer,
                                                                            final Optional<PreferenceWithHost> src) {
        final Optional<PreferencePathPointer> nextPreferencePathPointer = preferencePathPointer.next();
        if (nextPreferencePathPointer.isPresent()) {
            continueNavigationInActivity(classNameOfActivity, nextPreferencePathPointer.get());
            return Optional.empty();
        }
        return Optional.of(getPreferenceWithHost(preferencePathPointer.dereference(), src).host());
    }

    private void continueNavigationInActivity(final String classNameOfActivity,
                                              final PreferencePathPointer preferencePathPointer) {
        context.startActivity(
                createIntent(
                        classNameOfActivity,
                        preferencePathPointer));
    }

    private Intent createIntent(final String classNameOfReferencedActivity,
                                final PreferencePathPointer preferencePathPointer) {
        final Intent intent =
                new Intent(
                        context,
                        Utils.getClass(classNameOfReferencedActivity));
        final PreferencePathNavigatorData preferencePathNavigatorData =
                new PreferencePathNavigatorData(
                        preferencePathPointer.preferencePath().getPreference().getId(),
                        preferencePathPointer.indexWithinPreferencePath());
        intent.putExtras(preferencePathNavigatorData.toBundle());
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
        return fragmentFactoryAndInitializer.instantiateAndInitializeFragment(
                preferenceFragment,
                src,
                context,
                fragments);
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
