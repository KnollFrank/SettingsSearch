package de.KnollFrank.lib.preferencesearch.preference;

import androidx.preference.Preference;

import java.util.function.Consumer;

public interface IClickablePreference {

    void setClickListener(final Consumer<Preference> clickListener);

    Consumer<Preference> getClickListener();

    void performClick();
}
