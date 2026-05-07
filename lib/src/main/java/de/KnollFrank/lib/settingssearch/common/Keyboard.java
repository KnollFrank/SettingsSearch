package de.KnollFrank.lib.settingssearch.common;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

// FK-TODO: refactor
public class Keyboard {

    private Keyboard() {
    }

    public static void showKeyboard(final Activity activity) {
        final View view = activity.getCurrentFocus();
        if (view != null) {
            showKeyboard(activity, view);
        }
    }

    public static void showKeyboard(final Activity activity, final View view) {
        final InputMethodManager inputMethodManager = getInputMethodManager(activity);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void hideKeyboard(final Activity activity) {
        hideKeyboard(activity, activity.getWindow().getDecorView());
    }

    public static void hideKeyboard(final Activity activity, final View view) {
        final InputMethodManager inputMethodManager = getInputMethodManager(activity);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private static InputMethodManager getInputMethodManager(final Activity activity) {
        return (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }
}