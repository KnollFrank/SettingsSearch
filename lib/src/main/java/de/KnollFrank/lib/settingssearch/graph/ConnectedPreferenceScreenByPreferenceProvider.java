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

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceFragmentOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.ActivityDescription;
import de.KnollFrank.lib.settingssearch.common.Classes;
import de.KnollFrank.lib.settingssearch.common.Intents;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.common.converter.BundleConverter;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnectedToPreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;

class ConnectedPreferenceScreenByPreferenceProvider implements ChildNodeByEdgeValueProvider<PreferenceScreenOfHostOfActivity, Preference> {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceFragmentConnectedToPreferenceProvider preferenceFragmentConnectedToPreferenceProvider;
    private final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider;
    private final Context context;

    public ConnectedPreferenceScreenByPreferenceProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                                         final PreferenceFragmentConnectedToPreferenceProvider preferenceFragmentConnectedToPreferenceProvider,
                                                         final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider,
                                                         final Context context) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
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
                                        preference -> getConnectedPreferenceScreen(preference, preferenceScreenOfHostOfActivity.asPreferenceFragmentOfActivity()))));
    }

    private Optional<PreferenceScreenOfHostOfActivity> getConnectedPreferenceScreen(final Preference preference,
                                                                                    final PreferenceFragmentOfActivity hostOfPreference) {
        return this
                .getConnectedFragmentClass(preference, hostOfPreference)
                .flatMap(
                        fragmentClassConnectedToPreference ->
                                preferenceScreenWithHostProvider.getPreferenceScreenWithHostOfFragment(
                                        fragmentClassConnectedToPreference,
                                        Optional.of(new PreferenceOfHost(preference, hostOfPreference.preferenceFragment()))));
    }

    private Optional<? extends FragmentClassOfActivity<? extends Fragment>> getConnectedFragmentClass(
            final Preference preference,
            final PreferenceFragmentOfActivity hostOfPreference) {
        {
            final var fragmentClassFromFragmentClassName =
                    getFragmentClassFromFragmentClassName(
                            Optional.ofNullable(preference.getFragment()),
                            hostOfPreference.activityOfPreferenceFragment());
            if (fragmentClassFromFragmentClassName.isPresent()) {
                return fragmentClassFromFragmentClassName;
            }
        }
        {
            final var fragmentClassFromIntent =
                    getFragmentClassFromIntent(
                            Optional.ofNullable(preference.getIntent()));
            if (fragmentClassFromIntent.isPresent()) {
                return fragmentClassFromIntent;
            }
        }
        return getPreferenceFragmentClassConnectedToPreference(preference, hostOfPreference);
    }

    private Optional<? extends FragmentClassOfActivity<? extends PreferenceFragmentCompat>> getPreferenceFragmentClassConnectedToPreference(
            final Preference preference,
            final PreferenceFragmentOfActivity hostOfPreference) {
        return preferenceFragmentConnectedToPreferenceProvider
                .getPreferenceFragmentConnectedToPreference(
                        preference,
                        hostOfPreference.preferenceFragment())
                .map(preferenceFragmentConnectedToPreference ->
                             new FragmentClassOfActivity<>(
                                     preferenceFragmentConnectedToPreference,
                                     hostOfPreference.activityOfPreferenceFragment()));
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
