package de.KnollFrank.lib.settingssearch.common;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class Classes {

    public static <T> Class<? extends T> getClass(final String className) {
        try {
            return (Class<? extends T>) Class.forName(className);
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Class<?> loadClass(final String className, final Context context) {
        try {
            return context.getClassLoader().loadClass(className);
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException("ClassNotFoundException " + className, e);
        }
    }

    public static Class<? extends Fragment> loadFragmentClass(final String fragmentClassName, final Context context) {
        return FragmentFactory.loadFragmentClass(context.getClassLoader(), fragmentClassName);
    }

    // adapted from androidx.fragment.app.Fragment.instantiate()
    // FK-TODO: setzte die Punkte in der @deprecated-Warnung von androidx.fragment.app.Fragment.instantiate() um.
    public static <T extends Fragment> T instantiateFragmentClass(final Class<T> fragmentClass,
                                                                  final Optional<Bundle> arguments) {
        try {
            return _instantiateFragmentClass(fragmentClass, arguments);
        } catch (final InstantiationException | IllegalAccessException e) {
            throw new Fragment.InstantiationException("Unable to instantiate fragment " + fragmentClass
                    + ": make sure class name exists, is public, and has an"
                    + " empty constructor that is public", e);
        } catch (final NoSuchMethodException e) {
            throw new Fragment.InstantiationException("Unable to instantiate fragment " + fragmentClass
                    + ": could not find Fragment constructor", e);
        } catch (final InvocationTargetException e) {
            throw new Fragment.InstantiationException("Unable to instantiate fragment " + fragmentClass
                    + ": calling Fragment constructor caused an exception", e);
        }
    }

    private static <T extends Fragment> T _instantiateFragmentClass(final Class<T> fragmentClass,
                                                                    final Optional<Bundle> arguments)
            throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        final T fragment = fragmentClass.getConstructor().newInstance();
        arguments.ifPresent(
                _arguments -> {
                    _arguments.setClassLoader(fragment.getClass().getClassLoader());
                    fragment.setArguments(_arguments);
                });
        return fragment;
    }
}
