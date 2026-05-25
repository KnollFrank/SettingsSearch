package de.KnollFrank.lib.settingssearch.fragment.factory;

import androidx.fragment.app.Fragment;

import java.util.Optional;

record Arguments(Class<? extends Fragment> fragmentClass,
                 Optional<String> keyOfPreference,
                 Optional<Fragment> hostOfPreference) {
}
