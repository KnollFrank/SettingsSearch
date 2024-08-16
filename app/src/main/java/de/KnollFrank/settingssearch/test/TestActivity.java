package de.KnollFrank.settingssearch.test;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import de.KnollFrank.settingssearch.R;

public class TestActivity extends AppCompatActivity {

    public static final @IdRes int FRAGMENT_CONTAINER_VIEW = R.id.testFragmentContainerView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
    }
}
