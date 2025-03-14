package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;

public class PreferencePathNavigator {

    private final Context context;
    private final PreferenceWithHostProvider preferenceWithHostProvider;
    private final ContinueNavigationInActivity continueNavigationInActivity;
    private final PrincipalProvider principalProvider;

    public PreferencePathNavigator(final Context context,
                                   final PreferenceWithHostProvider preferenceWithHostProvider,
                                   final ContinueNavigationInActivity continueNavigationInActivity,
                                   final PrincipalProvider principalProvider) {
        this.context = context;
        this.preferenceWithHostProvider = preferenceWithHostProvider;
        this.continueNavigationInActivity = continueNavigationInActivity;
        this.principalProvider = principalProvider;
    }

    public Optional<? extends Fragment> navigatePreferencePath(final PreferencePathPointer preferencePathPointer) {
        return tryGetPrincipalOfHost(navigatePreferences(preferencePathPointer, Optional.empty()));
    }

    private Optional<PreferenceWithHost> navigatePreferences(final PreferencePathPointer preferencePathPointer,
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

    private Optional<PreferenceWithHost> navigatePreferences(final Optional<PreferencePathPointer> preferencePathPointer,
                                                             final PreferenceWithHost src) {
        return preferencePathPointer.isEmpty() ?
                Optional.of(src) :
                navigatePreferences(preferencePathPointer.orElseThrow(), Optional.of(src));
    }

    private Optional<Fragment> tryGetPrincipalOfHost(final Optional<PreferenceWithHost> preferenceWithHost) {
        return preferenceWithHost
                .flatMap(preferenceWithProxy -> principalProvider.getPrincipal(preferenceWithProxy.host(), preferenceWithHost))
                .or(() -> preferenceWithHost.map(PreferenceWithHost::host));
    }
}
