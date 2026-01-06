package de.KnollFrank.lib.settingssearch.common;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.annotation.IdRes;

public class Views {

    public static final @IdRes int ROOT_VIEW_CONTAINER_ID = android.R.id.content;

    public static ViewGroup getRootViewContainer(final Activity activity) {
        return activity.findViewById(ROOT_VIEW_CONTAINER_ID);
    }
}
