package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import static com.bytehamster.lib.preferencesearch.common.ListUtils.head;
import static com.bytehamster.lib.preferencesearch.common.ListUtils.tail;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bytehamster.lib.preferencesearch.SearchPreferenceResult;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResultListener;
import com.bytehamster.preferencesearch.R;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiplePreferenceScreensExample extends AppCompatActivity implements SearchPreferenceResultListener {

    public static final String FRAGMENT_NAVIGATION_PATH = "fragmentNavigationPath";
    public static final String KEY_OF_PREFERENCE_2_HIGHLIGHT = "keyOfPreference2Highlight";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        show(new PrefsFragmentFirst(), false);
    }

    @Override
    public void onSearchResultClicked(final SearchPreferenceResult result) {
        show(createFragmentNavigationPath(result.getResourceFile()), result.getKey(), true);
    }

    public void show(final List<String> fragmentNavigationPath, final String key, final boolean addToBackStack) {
        show(
                createFragment(fragmentNavigationPath, key),
                addToBackStack);
    }

    private static List<String> createFragmentNavigationPath(@IdRes final int resourceFile) {
        return _createFragmentNavigationPath(resourceFile)
                .stream()
                .map(Class::getName)
                .collect(Collectors.toList());
    }

    private static List<Class<?>> _createFragmentNavigationPath(@IdRes final int resourceFile) {
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

    private Fragment createFragment(final List<String> fragmentNavigationPath, final String key) {
        return Fragment.instantiate(
                this,
                head(fragmentNavigationPath),
                createArguments(
                        tail(fragmentNavigationPath),
                        key));
    }

    private static Bundle createArguments(final List<String> fragmentNavigationPath,
                                          final String keyOfPreference2Highlight) {
        final Bundle arguments = new Bundle();
        arguments.putStringArrayList(FRAGMENT_NAVIGATION_PATH, new ArrayList<>(fragmentNavigationPath));
        arguments.putString(KEY_OF_PREFERENCE_2_HIGHLIGHT, keyOfPreference2Highlight);
        return arguments;
    }

    private void show(final Fragment fragment, final boolean addToBackStack) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack("fragment");
        }
        fragmentTransaction.commit();
    }
}
