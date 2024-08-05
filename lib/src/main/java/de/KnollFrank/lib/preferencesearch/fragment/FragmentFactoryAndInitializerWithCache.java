package de.KnollFrank.lib.preferencesearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.PreferenceWithHost;

public class FragmentFactoryAndInitializerWithCache {

    private final FragmentFactoryAndInitializer delegate;
    private final Map<Arguments, Fragment> fragmentByArguments = new HashMap<>();

    public FragmentFactoryAndInitializerWithCache(final FragmentFactoryAndInitializer delegate) {
        this.delegate = delegate;
    }

    public Fragment instantiateAndInitializeFragment(final String fragmentClassName,
                                                     final Optional<PreferenceWithHost> src,
                                                     final Context context) {
        final Arguments arguments = ArgumentsFactory.createArguments(fragmentClassName, src);
        if (!fragmentByArguments.containsKey(arguments)) {
            final Fragment fragment = delegate.instantiateAndInitializeFragment(fragmentClassName, src, context);
            fragmentByArguments.put(arguments, fragment);
        }
        return fragmentByArguments.get(arguments);
    }

    private static class ArgumentsFactory {

        public static Arguments createArguments(final String fragmentClassName,
                                                final Optional<PreferenceWithHost> preferenceWithHost) {
            return new Arguments(
                    fragmentClassName,
                    getKeyOfPreference(preferenceWithHost),
                    getHostOfPreference(preferenceWithHost));
        }

        private static Optional<String> getKeyOfPreference(final Optional<PreferenceWithHost> preferenceWithHost) {
            return preferenceWithHost
                    .map(_preferenceWithHost -> _preferenceWithHost.preference)
                    .map(Preference::getKey);
        }

        private static Optional<Class<? extends PreferenceFragmentCompat>> getHostOfPreference(final Optional<PreferenceWithHost> preferenceWithHost) {
            return preferenceWithHost.map(_preferenceWithHost -> _preferenceWithHost.host);
        }
    }

    private static class Arguments {

        private final String fragmentClassName;
        private final Optional<String> keyOfPreference;
        private final Optional<Class<? extends PreferenceFragmentCompat>> hostOfPreference;

        public Arguments(final String fragmentClassName,
                         final Optional<String> keyOfPreference,
                         final Optional<Class<? extends PreferenceFragmentCompat>> hostOfPreference) {
            this.fragmentClassName = fragmentClassName;
            this.keyOfPreference = keyOfPreference;
            this.hostOfPreference = hostOfPreference;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Arguments arguments = (Arguments) o;
            return Objects.equals(fragmentClassName, arguments.fragmentClassName) && Objects.equals(keyOfPreference, arguments.keyOfPreference) && Objects.equals(hostOfPreference, arguments.hostOfPreference);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fragmentClassName, keyOfPreference, hostOfPreference);
        }

        @Override
        public String toString() {
            return "Arguments{" +
                    "fragmentClassName='" + fragmentClassName + '\'' +
                    ", keyOfPreference=" + keyOfPreference +
                    ", hostOfPreference=" + hostOfPreference +
                    '}';
        }
    }
}
