package de.KnollFrank.lib.settingssearch.results;

import androidx.recyclerview.widget.RecyclerView;

import java.util.OptionalInt;

public interface SettingsFragment {

    RecyclerView getRecyclerView();

    OptionalInt getPositionOfSetting(String keyOfSetting);
}
