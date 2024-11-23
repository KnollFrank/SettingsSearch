package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.view.View;
import android.widget.TextView;

import java.util.Optional;

class TextViews {

    public static void setOptionalTextOnOptionalTextView(
            final Optional<TextView> optionalTextView,
            final Optional<CharSequence> optionalText) {
        optionalTextView.ifPresent(
                textView -> {
                    optionalText.ifPresent(textView::setText);
                    textView.setVisibility(optionalText.isPresent() ? View.VISIBLE : View.GONE);
                });
    }
}
