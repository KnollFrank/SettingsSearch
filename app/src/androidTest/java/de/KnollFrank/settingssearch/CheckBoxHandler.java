package de.KnollFrank.settingssearch;

import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;

abstract class CheckBoxHandler {

    public static CheckBoxHandler of(final String key,
                                     final ViewInteraction preferencesContainer,
                                     final ViewAction clickCheckBox) {
        return new CheckBoxHandler() {

            @Override
            protected boolean isCheckBoxChecked() {
                return getSharedPreferences().getBoolean(key, false);
            }

            @Override
            protected void clickCheckBox() {
                preferencesContainer.perform(clickCheckBox);
            }

            private static SharedPreferences getSharedPreferences() {
                return PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext());
            }
        };
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

    protected abstract boolean isCheckBoxChecked();

    protected abstract void clickCheckBox();
}
