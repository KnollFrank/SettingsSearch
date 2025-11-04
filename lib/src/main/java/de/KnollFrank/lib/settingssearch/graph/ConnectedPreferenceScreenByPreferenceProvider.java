package de.KnollFrank.lib.settingssearch.graph;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.Classes;
import de.KnollFrank.lib.settingssearch.common.Intents;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;

class ConnectedPreferenceScreenByPreferenceProvider {

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

    public Map<Preference, PreferenceScreenWithHost> getConnectedPreferenceScreenByPreference(final PreferenceScreenWithHost preferenceScreenWithHost) {
        return Maps.filterPresentValues(
                Preferences
                        .getChildrenRecursively(preferenceScreenWithHost.preferenceScreen())
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        Function.identity(),
                                        preference -> getConnectedPreferenceScreen(preference, preferenceScreenWithHost.host()))));
    }

    private Optional<PreferenceScreenWithHost> getConnectedPreferenceScreen(final Preference preference,
                                                                            final PreferenceFragmentCompat hostOfPreference) {
        return this
                .getConnectedPreferenceFragment(preference, hostOfPreference)
                .flatMap(
                        fragmentConnectedToPreference ->
                                preferenceScreenWithHostProvider
                                        .getPreferenceScreenWithHostOfFragment(
                                                fragmentConnectedToPreference,
                                                Optional.of(new PreferenceWithHost(preference, hostOfPreference))));
    }

    private Optional<Class<? extends Fragment>> getConnectedPreferenceFragment(final Preference preference, final PreferenceFragmentCompat hostOfPreference) {
        return ConnectedPreferenceScreenByPreferenceProvider
                .loadFragmentClass(preference, context)
                .or(() -> getRootPreferenceFragment(Optional.ofNullable(preference.getIntent())))
                .or(() -> preferenceFragmentConnected2PreferenceProvider.getPreferenceFragmentConnected2Preference(preference, hostOfPreference));
    }

    private static Optional<Class<? extends Fragment>> loadFragmentClass(final Preference preference, final Context context) {
        return Optional
                .ofNullable(preference.getFragment())
                .map(fragmentClassName -> Classes.loadFragmentClass(fragmentClassName, context));
    }

    private Optional<Class<? extends PreferenceFragmentCompat>> getRootPreferenceFragment(final Optional<Intent> intent) {
        return intent
                .flatMap(_intent -> Intents.getClassName(_intent, context.getPackageManager()))
                .flatMap(this::asActivityClass)
                .flatMap(rootPreferenceFragmentOfActivityProvider::getRootPreferenceFragmentOfActivity);
    }

    private Optional<Class<? extends Activity>> asActivityClass(final String className) {
        return Classes.classNameAsSubclassOfClazz(className, Activity.class, context);
    }
}
