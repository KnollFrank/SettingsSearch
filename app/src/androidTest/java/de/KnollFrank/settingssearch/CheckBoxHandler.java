package de.KnollFrank.settingssearch;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;

import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;

class CheckBoxHandler {

    private final int position;
    private final String key;
    private final ViewInteraction preferencesContainer;

    public CheckBoxHandler(final int position, final String key, final ViewInteraction preferencesContainer) {
        this.position = position;
        this.key = key;
        this.preferencesContainer = preferencesContainer;
    }

    public void checkCheckBoxExplicitly() {
        uncheckCheckBox();
        checkCheckBox();
    }

    public void uncheckCheckBoxExplicitly() {
        checkCheckBox();
        uncheckCheckBox();
    }

    private void checkCheckBox() {
        if (!isCheckBoxChecked()) {
            clickCheckBox();
        }
    }

    private void uncheckCheckBox() {
        if (isCheckBoxChecked()) {
            clickCheckBox();
        }
    }

    private boolean isCheckBoxChecked() {
        return getSharedPreferences().getBoolean(key, false);
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext());
    }

    private void clickCheckBox() {
        preferencesContainer.perform(actionOnItemAtPosition(position, click()));
    }
}
