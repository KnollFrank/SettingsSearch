package de.KnollFrank.lib.settingssearch.common.task;

import android.app.Activity;

public class BlockingOnUiThreadRunnerFactory {

    public static BlockingOnUiThreadRunner fromActivity(final Activity activity) {
        return new BlockingOnUiThreadRunner(activity::runOnUiThread);
    }
}
