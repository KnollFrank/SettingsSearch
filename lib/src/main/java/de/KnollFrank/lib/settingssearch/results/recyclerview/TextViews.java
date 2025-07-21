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

    public static void setTextOnOptionalTextView(
            final Optional<TextView> optionalTextView,
            final CharSequence text) {
        // FK-TODO: refactor by invoking setOptionalTextOnOptionalTextView()
        optionalTextView.ifPresent(
                textView -> {
                    textView.setText(text);
                    textView.setVisibility(View.VISIBLE);
                });
    }
}
