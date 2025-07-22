package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferencePath;
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

    public Optional<? extends Fragment> navigatePreferencePath(final PreferencePath preferencePath) {
        return tryGetPrincipalOfHost(navigatePreferences(preferencePath, Optional.empty()));
    }

    private Optional<PreferenceWithHost> navigatePreferences(final PreferencePath preferencePath,
                                                             final Optional<PreferenceWithHost> src) {
        final Optional<Class<? extends Activity>> activity =
                preferencePath
                        .getStart()
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

    private Optional<PreferenceWithHost> navigatePreferences(final Optional<PreferencePath> preferencePath,
                                                             final PreferenceWithHost src) {
        return preferencePath.isEmpty() ?
                Optional.of(src) :
                navigatePreferences(preferencePath.orElseThrow(), Optional.of(src));
    }

    private Optional<Fragment> tryGetPrincipalOfHost(final Optional<PreferenceWithHost> preferenceWithHost) {
        return preferenceWithHost
                .flatMap(preferenceWithProxy -> principalProvider.getPrincipal(preferenceWithProxy.host(), preferenceWithHost))
                .or(() -> preferenceWithHost.map(PreferenceWithHost::host));
    }
}
