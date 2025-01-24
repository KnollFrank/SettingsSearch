package de.KnollFrank.settingssearch;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

// FK-FIXME: search for signature2, click search result, press back button multiple times => stays at SettingsActivity2 but should go back.
public class SettingsActivity2 extends AppCompatActivity {

    private static final @IdRes int FRAGMENT_CONTAINER_VIEW_ID = R.id.settings;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity2);
        if (savedInstanceState == null) {
            this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(FRAGMENT_CONTAINER_VIEW_ID, new SettingsFragment2())
                    .commit();
        }
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        final var continueWithPreferencePathNavigation =
                new ContinueWithPreferencePathNavigation(
                        this,
                        FRAGMENT_CONTAINER_VIEW_ID);
        continueWithPreferencePathNavigation.onStart();
    }

    public static class SettingsFragment2 extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences2, rootKey);
        }
    }
}