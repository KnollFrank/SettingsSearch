package de.KnollFrank.lib.settingssearch.common;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Keyboard {

    public static void showKeyboard(final Activity activity, final View view) {
        final InputMethodManager inputMethodManager = getInputMethodManager(activity);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private static InputMethodManager getInputMethodManager(final Activity activity) {
        return (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }
}
