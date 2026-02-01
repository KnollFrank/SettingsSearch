package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceOfHost;
import de.KnollFrank.lib.settingssearch.PreferencePath;

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

    public Optional<? extends Fragment> navigatePreferencePath(final PreferencePath preferencePath) {
        return tryGetPrincipalOfHost(navigatePreferences(preferencePath, Optional.empty()));
    }

    private Optional<PreferenceOfHost> navigatePreferences(final PreferencePath preferencePath,
                                                           final Optional<PreferenceOfHost> src) {
        final Optional<Class<? extends Activity>> activity =
                preferencePath
                        .getStart()
                        .searchablePreference()
                        .getClassOfReferencedActivity(context);
        return activity.isPresent() ?
                continueNavigationInActivity.continueNavigationInActivity(
                        activity.orElseThrow(),
                        preferencePath,
                        src) :
                navigatePreferences(
                        preferencePath.getTail(),
                        preferenceWithHostProvider.getPreferenceWithHost(preferencePath.getStart(), src));
    }

    private Optional<PreferenceOfHost> navigatePreferences(final Optional<PreferencePath> preferencePath,
                                                           final PreferenceOfHost src) {
        return preferencePath.isEmpty() ?
                Optional.of(src) :
                navigatePreferences(preferencePath.orElseThrow(), Optional.of(src));
    }

    private Optional<Fragment> tryGetPrincipalOfHost(final Optional<PreferenceOfHost> preferenceWithHost) {
        return preferenceWithHost
                .flatMap(preferenceWithProxy -> principalProvider.getPrincipal(preferenceWithProxy.hostOfPreference(), preferenceWithHost))
                .or(() -> preferenceWithHost.map(PreferenceOfHost::hostOfPreference));
    }
}
