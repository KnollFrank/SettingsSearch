package de.KnollFrank.lib.preferencesearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class FragmentFactoryAndInitializerWithCache {

    private final FragmentFactoryAndInitializer delegate;
    private final Map<Arguments, Fragment> fragmentByArguments = new HashMap<>();

    public FragmentFactoryAndInitializerWithCache(final FragmentFactoryAndInitializer delegate) {
        this.delegate = delegate;
    }

    public Fragment instantiateAndInitializeFragment(final String fragmentClassName,
                                                     final Optional<Preference> src,
                                                     final Context context) {
        final Arguments arguments = Arguments.createArguments(fragmentClassName, src);
        if (!fragmentByArguments.containsKey(arguments)) {
            final Fragment fragment = delegate.instantiateAndInitializeFragment(fragmentClassName, src, context);
            fragmentByArguments.put(arguments, fragment);
        }
        return fragmentByArguments.get(arguments);
    }

    private static class Arguments {

        private final String fragmentClassName;
        private final String keyOfPreference;

        private Arguments(final String fragmentClassName, final String keyOfPreference) {
            this.fragmentClassName = fragmentClassName;
            this.keyOfPreference = keyOfPreference;
        }

        public static Arguments createArguments(final String fragmentClassName,
                                                final Optional<Preference> src) {
            return new Arguments(
                    fragmentClassName,
                    src
                            .map(Preference::getKey)
                            .orElse(null));
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Arguments arguments = (Arguments) o;
            return Objects.equals(fragmentClassName, arguments.fragmentClassName) && Objects.equals(keyOfPreference, arguments.keyOfPreference);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fragmentClassName, keyOfPreference);
        }

        @Override
        public String toString() {
            return "Arguments{" +
                    "fragmentClassName='" + fragmentClassName + '\'' +
                    ", keyOfPreference='" + keyOfPreference + '\'' +
                    '}';
        }
    }
}
