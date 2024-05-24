package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import static com.bytehamster.lib.preferencesearch.common.ListUtils.head;
import static com.bytehamster.lib.preferencesearch.common.ListUtils.tail;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.preferencesearch.R;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Navigation {

    public static final String NAVIGATION_PATH = "navigationPath";
    public static final String KEY_OF_PREFERENCE_2_HIGHLIGHT = "keyOfPreference2Highlight";

    public static void navigatePathAndHighlightPreference(final List<String> navigationPath,
                                                          final String key,
                                                          final boolean addToBackStack,
                                                          final FragmentActivity fragmentActivity) {
        show(
                createFragment(navigationPath, key, fragmentActivity),
                addToBackStack,
                fragmentActivity.getSupportFragmentManager());
    }

    public static void show(final Fragment fragment,
                            final boolean addToBackStack,
                            final FragmentManager fragmentManager) {
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack("fragment");
        }
        fragmentTransaction.commit();
    }

    public static List<String> createFragmentNavigationPath(@IdRes final int resourceFile) {
        return _createFragmentNavigationPath(resourceFile)
                .stream()
                .map(Class::getName)
                .collect(Collectors.toList());
    }

    private static List<Class<? extends PreferenceFragmentCompat>> _createFragmentNavigationPath(@IdRes final int resourceFile) {
        switch (resourceFile) {
            case R.xml.preferences_multiple_screens:
                return ImmutableList.of(PrefsFragmentFirst.class);
            case R.xml.preferences2:
                return ImmutableList.of(PrefsFragmentFirst.class, PrefsFragmentSecond.class);
            case R.xml.preferences3:
                return ImmutableList.of(PrefsFragmentFirst.class, PrefsFragmentSecond.class, PrefsFragmentThird.class);
            default:
                throw new IllegalArgumentException();
        }
    }

    private static Fragment createFragment(final List<String> navigationPath,
                                           final String key,
                                           final Context context) {
        return Fragment.instantiate(
                context,
                head(navigationPath),
                createArguments(tail(navigationPath), key));
    }

    private static Bundle createArguments(final List<String> navigationPath,
                                          final String keyOfPreference2Highlight) {
        final Bundle arguments = new Bundle();
        arguments.putStringArrayList(NAVIGATION_PATH, new ArrayList<>(navigationPath));
        arguments.putString(KEY_OF_PREFERENCE_2_HIGHLIGHT, keyOfPreference2Highlight);
        return arguments;
    }
}
