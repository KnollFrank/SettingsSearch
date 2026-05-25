package de.KnollFrank.lib.settingssearch.results;

public interface Setting {

    String getId();

    String getKey();

    boolean hasPreferenceMatchWithinSearchableInfo();
}
