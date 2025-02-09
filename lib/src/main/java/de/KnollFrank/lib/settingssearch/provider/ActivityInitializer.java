package de.KnollFrank.lib.settingssearch.provider;

import android.os.Bundle;

import java.util.Optional;

public interface ActivityInitializer {

    void beforeStartActivity();

    Optional<Bundle> createExtras();
}
