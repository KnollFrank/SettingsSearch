package de.KnollFrank.lib.preferencesearch.fragment;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class Fragments {

    private final FragmentFactory fragmentFactory;
    private final FragmentInitializer fragmentInitializer;
    private final Context context;
    private final Map<Arguments, Fragment> fragmentByArguments = new HashMap<>();

    public Fragments(final FragmentFactory fragmentFactory,
                     final FragmentInitializer fragmentInitializer,
                     final Context context) {
        this.fragmentFactory = fragmentFactory;
        this.fragmentInitializer = fragmentInitializer;
        this.context = context;
    }

    public Fragment instantiateAndInitializeFragment(final String fragmentClassName,
                                                     final Optional<Preference> src) {
        final Arguments arguments = Arguments.createArguments(fragmentClassName, src);
        if (!fragmentByArguments.containsKey(arguments)) {
            final Fragment fragment = _instantiateAndInitializeFragment(fragmentClassName, src);
            fragmentByArguments.put(arguments, fragment);
        }
        return fragmentByArguments.get(arguments);
    }

    private Fragment _instantiateAndInitializeFragment(final String fragmentClassName,
                                                       final Optional<Preference> src) {
        final Fragment fragment = fragmentFactory.instantiate(fragmentClassName, src, context);
        fragmentInitializer.initialize(fragment);
        return fragment;
    }

    public static <T extends Fragment> void showFragment(final T fragment,
                                                         final Consumer<T> onFragmentShown,
                                                         final boolean addToBackStack,
                                                         final @IdRes int containerViewId,
                                                         final FragmentManager fragmentManager) {
        final FragmentTransaction fragmentTransaction =
                fragmentManager
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(containerViewId, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        executeOnceOnFragmentStarted(fragment, onFragmentShown, fragmentManager);
        fragmentTransaction.commit();
    }

    private static <T extends Fragment> void executeOnceOnFragmentStarted(
            final T fragment,
            final Consumer<T> onFragmentStarted,
            final FragmentManager fragmentManager) {
        fragmentManager.registerFragmentLifecycleCallbacks(
                new FragmentLifecycleCallbacks() {

                    @Override
                    public void onFragmentStarted(@NonNull final FragmentManager fragmentManager,
                                                  @NonNull final Fragment _fragment) {
                        if (_fragment == fragment) {
                            fragmentManager.unregisterFragmentLifecycleCallbacks(this);
                            onFragmentStarted.accept(fragment);
                        }
                    }
                },
                false);
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
