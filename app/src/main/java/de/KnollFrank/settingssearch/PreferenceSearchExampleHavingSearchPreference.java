package de.KnollFrank.settingssearch;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirstHavingSearchPreference;

public class PreferenceSearchExampleHavingSearchPreference extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_preference_screens_example);
        if (savedInstanceState == null) {
            show(new PrefsFragmentFirstHavingSearchPreference());
        }
    }

    private void show(final Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }
}
