package de.KnollFrank.settingssearch;

import android.app.Activity;
import android.content.Intent;
import android.os.BaseBundle;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.ActivityDescription;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.InitializePreferenceFragmentWithActivityDescriptionBeforeOnCreate;

public class SettingsActivity extends AppCompatActivity {

    private static final @IdRes int fragmentContainerViewId = View.generateViewId();
    public static final String SETTINGS_ACTIVITY_MANDATORY_DUMMY_KEY = "SettingsActivity.mandatoryDummyKey";
    public static final String PREFERENCE_WITH_DYNAMIC_TITLE_KEY = "preferenceWithDynamicTitle";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
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
        if (!getIntent().getExtras().containsKey(SETTINGS_ACTIVITY_MANDATORY_DUMMY_KEY)) {
            throw new IllegalStateException();
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements InitializePreferenceFragmentWithActivityDescriptionBeforeOnCreate {

        private Optional<String> dynamicTitle = Optional.empty();

        @Override
        public void initializePreferenceFragmentWithActivityDescriptionBeforeOnCreate(final ActivityDescription activityDescription) {
            dynamicTitle = getDynamicTitle(activityDescription.arguments());
        }

        @Override
        public void onCreate(@Nullable final Bundle savedInstanceState) {
            final Activity activity = getActivity();
            if (activity instanceof SettingsActivity) {
                dynamicTitle =
                        Optional
                                .ofNullable(activity.getIntent())
                                .map(Intent::getExtras)
                                .flatMap(this::getDynamicTitle);
            }
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            getPreferenceScreen().findPreference("preferenceWithIntent").setIntent(new Intent(getContext(), SettingsActivity2.class));
            dynamicTitle.ifPresent(
                    _dynamicTitle ->
                            this
                                    .getPreferenceScreen()
                                    .findPreference(SettingsActivity.PREFERENCE_WITH_DYNAMIC_TITLE_KEY)
                                    .setTitle(_dynamicTitle));
        }

        private Optional<String> getDynamicTitle(final BaseBundle bundle) {
            return Optional.ofNullable(bundle.getString(SettingsActivity.PREFERENCE_WITH_DYNAMIC_TITLE_KEY));
        }
    }
}