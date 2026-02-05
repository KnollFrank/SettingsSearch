package de.KnollFrank.lib.settingssearch.graph;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.ActivityDescription;
import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.common.Classes;
import de.KnollFrank.lib.settingssearch.common.Intents;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.common.converter.BundleConverter;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnectedToPreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;

class ConnectedPreferenceScreenByPreferenceProvider implements ChildNodeByEdgeValueProvider<PreferenceScreenOfHostOfActivity, Preference> {

    private final PreferenceScreenProvider preferenceScreenProvider;
    private final PreferenceFragmentConnectedToPreferenceProvider preferenceFragmentConnectedToPreferenceProvider;
    private final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider;
    private final Context context;

    public ConnectedPreferenceScreenByPreferenceProvider(final PreferenceScreenProvider preferenceScreenProvider,
                                                         final PreferenceFragmentConnectedToPreferenceProvider preferenceFragmentConnectedToPreferenceProvider,
                                                         final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider,
                                                         final Context context) {
        this.preferenceScreenProvider = preferenceScreenProvider;
        this.preferenceFragmentConnectedToPreferenceProvider = preferenceFragmentConnectedToPreferenceProvider;
        this.rootPreferenceFragmentOfActivityProvider = rootPreferenceFragmentOfActivityProvider;
        this.context = context;
    }

    @Override
    public Map<Preference, PreferenceScreenOfHostOfActivity> getChildNodeOfNodeByEdgeValue(final PreferenceScreenOfHostOfActivity node) {
        return getConnectedPreferenceScreenByPreference(node);
    }

    public Map<Preference, PreferenceScreenOfHostOfActivity> getConnectedPreferenceScreenByPreference(final PreferenceScreenOfHostOfActivity preferenceScreenOfHostOfActivity) {
        return Maps.filterPresentValues(
                Preferences
                        .getChildrenRecursively(preferenceScreenOfHostOfActivity.preferenceScreen())
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        Function.identity(),
                                        preference ->
                                                getConnectedPreferenceScreen(
                                                        new PreferenceOfHostOfActivity(
                                                                preference,
                                                                preferenceScreenOfHostOfActivity.hostOfPreferenceScreen(),
                                                                preferenceScreenOfHostOfActivity.activityOfHost())))));
    }

    private Optional<PreferenceScreenOfHostOfActivity> getConnectedPreferenceScreen(final PreferenceOfHostOfActivity preferenceOfHostOfActivity) {
        return this
                .getConnectedFragmentClass(preferenceOfHostOfActivity)
                .flatMap(
                        fragmentClassConnectedToPreference ->
                                preferenceScreenProvider.getPreferenceScreen(
                                        fragmentClassConnectedToPreference,
                                        Optional.of(preferenceOfHostOfActivity)));
    }

    private Optional<? extends FragmentClassOfActivity<? extends Fragment>> getConnectedFragmentClass(final PreferenceOfHostOfActivity preferenceOfHostOfActivity) {
        {
            final var fragmentClassFromFragmentClassName =
                    getFragmentClassFromFragmentClassName(
                            Optional.ofNullable(preferenceOfHostOfActivity.preference().getFragment()),
                            preferenceOfHostOfActivity.activityOfHost());
            if (fragmentClassFromFragmentClassName.isPresent()) {
                return fragmentClassFromFragmentClassName;
            }
        }
        {
            final var fragmentClassFromIntent =
                    getFragmentClassFromIntent(
                            Optional.ofNullable(preferenceOfHostOfActivity.preference().getIntent()));
            if (fragmentClassFromIntent.isPresent()) {
                return fragmentClassFromIntent;
            }
        }
        return getPreferenceFragmentClassConnectedToPreference(preferenceOfHostOfActivity);
    }

    private Optional<? extends FragmentClassOfActivity<? extends PreferenceFragmentCompat>> getPreferenceFragmentClassConnectedToPreference(final PreferenceOfHostOfActivity preferenceOfHostOfActivity) {
        return preferenceFragmentConnectedToPreferenceProvider
                .getPreferenceFragmentConnectedToPreference(
                        preferenceOfHostOfActivity.preference(),
                        preferenceOfHostOfActivity.hostOfPreference())
                .map(preferenceFragmentConnectedToPreference ->
                             new FragmentClassOfActivity<>(
                                     preferenceFragmentConnectedToPreference,
                                     preferenceOfHostOfActivity.activityOfHost()));
    }

    private Optional<? extends FragmentClassOfActivity<? extends Fragment>> getFragmentClassFromFragmentClassName(
            final Optional<String> fragmentClassName,
            final ActivityDescription activityOFragment) {
        return fragmentClassName
                .<Class<? extends Fragment>>map(_fragmentClassName -> Classes.loadFragmentClass(_fragmentClassName, context))
                .map(fragmentClass ->
                             new FragmentClassOfActivity<>(
                                     fragmentClass,
                                     activityOFragment));
    }

    private Optional<? extends FragmentClassOfActivity<? extends PreferenceFragmentCompat>> getFragmentClassFromIntent(final Optional<Intent> intent) {
        return intent.flatMap(this::getFragmentClassFromIntent);
    }

    private Optional<? extends FragmentClassOfActivity<? extends PreferenceFragmentCompat>> getFragmentClassFromIntent(final Intent intent) {
        return Intents
                .getClassName(intent)
                .flatMap(this::asActivityClass)
                .map(activityClass ->
                             new ActivityDescription(
                                     activityClass,
                                     getPersistableExtras(intent)))
                .flatMap(activityDescription ->
                                 rootPreferenceFragmentOfActivityProvider
                                         .getRootPreferenceFragmentOfActivity(activityDescription.activity())
                                         .map(rootPreferenceFragmentOfActivity ->
                                                      new FragmentClassOfActivity<>(
                                                              rootPreferenceFragmentOfActivity,
                                                              activityDescription)));
    }

    private Optional<Class<? extends Activity>> asActivityClass(final String className) {
        return Classes.classNameAsSubclassOfClazz(className, Activity.class, context);
    }

    private static PersistableBundle getPersistableExtras(final Intent intent) {
        return Optional
                .ofNullable(intent.getExtras())
                .map(BundleConverter::toPersistableBundle)
                .orElseGet(PersistableBundle::new);
    }
}
