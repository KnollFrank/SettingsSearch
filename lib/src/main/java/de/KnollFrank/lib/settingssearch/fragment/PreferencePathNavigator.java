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

    public PreferencePathNavigator(final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                   final Context context) {
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.context = context;
    }

    public Optional<PreferenceFragmentCompat> navigatePreferencePath(final PreferencePathPointer preferencePathPointer) {
        return navigatePreferences(Optional.empty(), Optional.of(preferencePathPointer));
    }

    private Optional<PreferenceFragmentCompat> navigatePreferences(final Optional<PreferenceWithHost> src,
                                                                   final Optional<PreferencePathPointer> preferencePathPointer) {
        if (preferencePathPointer.isEmpty()) {
            return Optional.of(src.orElseThrow().host());
        }
        final PreferencePathPointer _preferencePathPointer = preferencePathPointer.get();
        final SearchablePreference actualPreference = _preferencePathPointer.dereference();
        if (actualPreference.getClassNameOfReferencedActivity().isPresent()) {
            final Optional<PreferencePathPointer> nextPreferencePathPointer = _preferencePathPointer.next();
            if (nextPreferencePathPointer.isPresent()) {
                continueNavigationInAnotherActivity(
                        actualPreference.getClassNameOfReferencedActivity().get(),
                        nextPreferencePathPointer.get());
                return Optional.empty();
            }
            return Optional.of(getPreferenceWithHost(actualPreference, src).host());
        }
        return navigatePreferences(
                Optional.of(getPreferenceWithHost(actualPreference, src)),
                _preferencePathPointer.next());
    }

    private void continueNavigationInAnotherActivity(final String classNameOfReferencedActivity,
                                                     final PreferencePathPointer preferencePathPointer) {
        context.startActivity(
                createIntent(
                        classNameOfReferencedActivity,
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
