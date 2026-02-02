package de.KnollFrank.lib.settingssearch.graph;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceFragmentClassOfActivity;
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
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;

class ConnectedPreferenceScreenByPreferenceProvider implements ChildNodeByEdgeValueProvider<PreferenceScreenOfHostOfActivity, Preference> {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider;
    private final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider;
    private final Context context;

    public ConnectedPreferenceScreenByPreferenceProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                                         final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
                                                         final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider,
                                                         final Context context) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceFragmentConnected2PreferenceProvider = preferenceFragmentConnected2PreferenceProvider;
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
                .getConnectedPreferenceFragment(preference, hostOfPreference)
                .flatMap(
                        fragmentConnectedToPreference ->
                                preferenceScreenWithHostProvider.getPreferenceScreenWithHostOfFragment(
                                        fragmentConnectedToPreference,
                                        Optional.of(new PreferenceOfHost(preference, hostOfPreference.preferenceFragment()))));
    }

    private Optional<FragmentClassOfActivity> getConnectedPreferenceFragment(final Preference preference,
                                                                             final PreferenceFragmentOfActivity hostOfPreference) {
        return ConnectedPreferenceScreenByPreferenceProvider
                .loadFragmentClass(preference, context)
                .map(fragmentClass -> new FragmentClassOfActivity(fragmentClass, hostOfPreference.activityOfPreferenceFragment()))
                .or(() ->
                            this
                                    .getRootPreferenceFragment(Optional.ofNullable(preference.getIntent()))
                                    .map(PreferenceFragmentClassOfActivity::asFragmentClassOfActivity))
                .or(() ->
                            preferenceFragmentConnected2PreferenceProvider
                                    .getPreferenceFragmentConnected2Preference(
                                            preference,
                                            hostOfPreference.preferenceFragment())
                                    .map(preferenceFragmentConnected2Preference ->
                                                 new FragmentClassOfActivity(
                                                         preferenceFragmentConnected2Preference,
                                                         hostOfPreference.activityOfPreferenceFragment())));
    }

    private static Optional<Class<? extends Fragment>> loadFragmentClass(final Preference preference, final Context context) {
        return Optional
                .ofNullable(preference.getFragment())
                .map(fragmentClassName -> Classes.loadFragmentClass(fragmentClassName, context));
    }

    private Optional<PreferenceFragmentClassOfActivity> getRootPreferenceFragment(final Optional<Intent> intent) {
        return intent.flatMap(this::getRootPreferenceFragment);
    }

    private Optional<PreferenceFragmentClassOfActivity> getRootPreferenceFragment(final Intent intent) {
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
                                                      new PreferenceFragmentClassOfActivity(
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
