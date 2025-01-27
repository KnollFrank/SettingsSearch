package de.KnollFrank.lib.settingssearch;

import de.KnollFrank.lib.settingssearch.fragment.Fragments;

@FunctionalInterface
public interface Fragment2PreferenceFragmentConverterFactory {

    Fragment2PreferenceFragmentConverter createFragment2PreferenceFragmentConverter(Fragments fragments);
}
