package de.KnollFrank.settingssearch.preference.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;

public class CustomDialogPreference extends DialogPreference {

    public CustomDialogPreference(@NonNull final Context context) {
        super(context);
    }

    public CustomDialogPreference(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }
}