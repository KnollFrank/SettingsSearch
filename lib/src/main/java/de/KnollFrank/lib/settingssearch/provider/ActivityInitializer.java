package de.KnollFrank.lib.settingssearch.provider;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.util.Optional;

public interface ActivityInitializer<T extends Fragment> {

    void beforeStartActivity(T src);

    Optional<Bundle> createExtras(T src);
}
