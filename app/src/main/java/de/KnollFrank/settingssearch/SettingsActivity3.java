package de.KnollFrank.settingssearch;

import static de.KnollFrank.lib.settingssearch.fragment.navigation.ContinueWithPreferencePathNavigation.continueWithPreferencePathNavigation;
import static de.KnollFrank.settingssearch.SettingsActivity.createSearchPreferenceFragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import de.KnollFrank.lib.settingssearch.common.Locales;
import de.KnollFrank.settingssearch.preference.fragment.ItemFragment3;

// FK-FIXME: suche nach "item3 20" und klicke auf das Suchergebnis, dann wird zwar zur Seite, die das Suchergebnis enthält, navigiert, aber es wird fälschlicherweise nicht zur Setting gescrollt.
public class SettingsActivity3 extends AppCompatActivity {

    private static final @IdRes int fragmentContainerViewId = View.generateViewId();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new ItemFragment3())
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
        continueWithPreferencePathNavigation(
                this,
                findViewById(R.id.settings_root),
                fragmentContainerViewId,
                onMergedPreferenceScreenAvailable ->
                        createSearchPreferenceFragments(
                                this,
                                onMergedPreferenceScreenAvailable,
                                fragmentContainerViewId),
                Locales.getCurrentLanguageLocale(getResources()));
    }
}