package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

public class PreferencePathNavigator {

    private final Context context;
    private final PreferenceWithHostProvider preferenceWithHostProvider;
    private final ContinueNavigationInActivity continueNavigationInActivity;
    private final Map<Class<? extends PreferenceFragmentCompat>, Class<? extends Fragment>> fragmentsConnected2PreferenceFragments;
    private final InstantiateAndInitializeFragment instantiateAndInitializeFragment;

    public PreferencePathNavigator(final Context context,
                                   final PreferenceWithHostProvider preferenceWithHostProvider,
                                   final ContinueNavigationInActivity continueNavigationInActivity,
                                   final Map<Class<? extends PreferenceFragmentCompat>, Class<? extends Fragment>> fragmentsConnected2PreferenceFragments,
                                   final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        this.context = context;
        this.preferenceWithHostProvider = preferenceWithHostProvider;
        this.continueNavigationInActivity = continueNavigationInActivity;
        this.fragmentsConnected2PreferenceFragments = fragmentsConnected2PreferenceFragments;
        this.instantiateAndInitializeFragment = instantiateAndInitializeFragment;
    }

    public Optional<? extends Fragment> navigatePreferencePath(final PreferencePathPointer preferencePathPointer) {
        final Optional<PreferenceFragmentCompat> preferenceFragment = navigatePreferences(preferencePathPointer, Optional.empty());
        return this
                .getConnectedFragment(preferenceFragment)
                .or(() -> preferenceFragment);
    }

    private Optional<PreferenceFragmentCompat> navigatePreferences(final PreferencePathPointer preferencePathPointer,
                                                                   final Optional<PreferenceWithHost> src) {
        final Optional<Class<? extends Activity>> activity =
                preferencePathPointer
                        .dereference()
                        .getClassOfReferencedActivity(context);
        return activity.isPresent() ?
                continueNavigationInActivity.continueNavigationInActivity(
                        activity.orElseThrow(),
                        preferencePathPointer,
                        src) :
                navigatePreferences(
                        preferencePathPointer.next(),
                        preferenceWithHostProvider.getPreferenceWithHost(preferencePathPointer.dereference(), src));
    }

    private Optional<PreferenceFragmentCompat> navigatePreferences(final Optional<PreferencePathPointer> preferencePathPointer,
                                                                   final PreferenceWithHost src) {
        return preferencePathPointer.isEmpty() ?
                Optional.of(src.host()) :
                navigatePreferences(preferencePathPointer.orElseThrow(), Optional.of(src));
    }

    // FK-TODO: extract interface from method
    private Optional<Fragment> getConnectedFragment(final Optional<PreferenceFragmentCompat> preferenceFragmentCompat) {
        return preferenceFragmentCompat
                .map(PreferenceFragmentCompat::getClass)
                .flatMap(preferenceFragment -> Maps.get(fragmentsConnected2PreferenceFragments, preferenceFragment))
                .map(fragment -> instantiateAndInitializeFragment.instantiateAndInitializeFragment((Class<Fragment>) fragment, Optional.empty()));
    }
}
