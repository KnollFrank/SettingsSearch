package de.KnollFrank.lib.settingssearch.provider;

import android.app.Activity;
import android.os.Bundle;

import java.util.Optional;

@FunctionalInterface
public interface ExtrasForActivityFactory {

    Optional<Bundle> createExtrasForActivity(final Class<? extends Activity> activity);
}
