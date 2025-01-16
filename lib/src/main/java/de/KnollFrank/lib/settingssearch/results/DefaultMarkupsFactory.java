package de.KnollFrank.lib.settingssearch.results;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.style.BackgroundColorSpan;
import android.text.style.TextAppearanceSpan;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.R;

public class DefaultMarkupsFactory implements MarkupsFactory {

    private final Context context;

    public DefaultMarkupsFactory(final Context context) {
        this.context = context;
    }

    @Override
    public List<Object> createMarkups() {
        final ImmutableList.Builder<Object> markupsBuilder = ImmutableList.builder();
        markupsBuilder.add(new TextAppearanceSpan(context, R.style.SearchPreferenceResultTextAppearance));
        this
                .getBackgroundColor()
                .map(BackgroundColorSpan::new)
                .ifPresent(markupsBuilder::add);
        return markupsBuilder.build();
    }

    private Optional<Integer> getBackgroundColor() {
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
