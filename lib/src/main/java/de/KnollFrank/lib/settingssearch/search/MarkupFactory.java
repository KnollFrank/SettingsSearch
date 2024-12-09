package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.style.BackgroundColorSpan;
import android.text.style.TextAppearanceSpan;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.R;

public class MarkupFactory {

    public static List<Object> createMarkups(final Context context) {
        final ImmutableList.Builder<Object> markupsBuilder = ImmutableList.builder();
        markupsBuilder.add(new TextAppearanceSpan(context, R.style.SearchPreferenceResultTextAppearance));
        MarkupFactory
                .getBackgroundColor(context)
                .map(BackgroundColorSpan::new)
                .ifPresent(markupsBuilder::add);
        return markupsBuilder.build();
    }

    private static Optional<Integer> getBackgroundColor(final Context context) {
        try (final TypedArray typedArray =
                     context.obtainStyledAttributes(
                             R.style.SearchPreferenceResultBackgroundColor,
                             R.styleable.SearchPreferenceResultBackgroundColor)) {
            final int backgroundColorAttr = R.styleable.SearchPreferenceResultBackgroundColor_backgroundColor;
            return typedArray.hasValue(backgroundColorAttr) ?
                    Optional.of(typedArray.getColor(backgroundColorAttr, Color.GREEN)) :
                    Optional.empty();
        }
    }
}
