package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.bytehamster.preferencesearch.R;

// FK-TODO: move to package com.bytehamster.preferencesearch.test
public class TestActivity extends AppCompatActivity {

    @IdRes
    public static final int FRAGMENT_CONTAINER_VIEW = R.id.fragmentContainerView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
    }
}
