package de.KnollFrank.lib.settingssearch.test;

import androidx.fragment.app.FragmentActivity;

import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;

import java.util.function.Consumer;

public class TestHelper {

    public static void doWithFragmentActivity(final Consumer<FragmentActivity> fragmentActivityConsumer) {
        try (final ActivityController<FragmentActivity> controller =
                     Robolectric
                             .buildActivity(FragmentActivity.class)
                             .setup()) {
            fragmentActivityConsumer.accept(controller.get());
        }
    }
}
