package de.KnollFrank.lib.settingssearch.db.preference.converter;

import android.content.Context;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.function.BiConsumer;

public class PreferenceFragmentTemplate extends PreferenceFragmentCompat {

    private final BiConsumer<PreferenceScreen, Context> addPreferences2Screen;

    public PreferenceFragmentTemplate(final BiConsumer<PreferenceScreen, Context> addPreferences2Screen) {
        this.addPreferences2Screen = addPreferences2Screen;
    }

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        final Context context = getPreferenceManager().getContext();
        final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
        screen.setTitle("screen title");
        screen.setSummary("screen summary");
        addPreferences2Screen.accept(screen, context);
        setPreferenceScreen(screen);
    }
}
