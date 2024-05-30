package com.bytehamster.lib.preferencesearch.common;

import android.view.View;

public class UIUtils {

    public static void set_VISIBLE_or_GONE(final View view, final boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
