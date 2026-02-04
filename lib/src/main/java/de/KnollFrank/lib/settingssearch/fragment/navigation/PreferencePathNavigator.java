package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.PreferencePath;

public class PreferencePathNavigator {

    private final Context context;
    private final PreferenceOfHostOfActivityProvider preferenceOfHostOfActivityProvider;
    private final ContinueNavigationInActivity continueNavigationInActivity;
    private final PrincipalProvider principalProvider;

    public PreferencePathNavigator(final Context context,
                                   final PreferenceOfHostOfActivityProvider preferenceOfHostOfActivityProvider,
                                   final ContinueNavigationInActivity continueNavigationInActivity,
                                   final PrincipalProvider principalProvider) {
        this.context = context;
        this.preferenceOfHostOfActivityProvider = preferenceOfHostOfActivityProvider;
        this.continueNavigationInActivity = continueNavigationInActivity;
        this.principalProvider = principalProvider;
    }

    public Optional<? extends Fragment> navigatePreferencePath(final PreferencePath preferencePath) {
        return tryGetPrincipalOfHost(navigatePreferences(preferencePath, Optional.empty()));
    }

    private Optional<PreferenceOfHostOfActivity> navigatePreferences(final PreferencePath preferencePath,
                                                                     final Optional<PreferenceOfHostOfActivity> src) {
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
                        preferenceOfHostOfActivityProvider.getPreferenceOfHostOfActivity(preferencePath.getStart(), src));
    }

    private Optional<PreferenceOfHostOfActivity> navigatePreferences(final Optional<PreferencePath> preferencePath,
                                                                     final PreferenceOfHostOfActivity src) {
        return preferencePath.isEmpty() ?
                Optional.of(src) :
                navigatePreferences(preferencePath.orElseThrow(), Optional.of(src));
    }

    private Optional<Fragment> tryGetPrincipalOfHost(final Optional<PreferenceOfHostOfActivity> preferenceOfHostOfActivity) {
        return preferenceOfHostOfActivity
                .map(PreferenceOfHostOfActivity::asPreferenceFragmentOfActivity)
                .flatMap(preferenceFragmentOfActivity ->
                                 principalProvider.getPrincipal(
                                         preferenceFragmentOfActivity,
                                         preferenceOfHostOfActivity))
                .or(() -> preferenceOfHostOfActivity.map(PreferenceOfHostOfActivity::hostOfPreference));
    }
}
