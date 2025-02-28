package de.KnollFrank.lib.settingssearch.results;

import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.Duration;

public class ItemOfRecyclerViewHighlighter implements SettingHighlighter {

    private final RecyclerView recyclerView;
    private final PositionOfSettingProvider positionOfSettingProvider;
    private final Duration highlightDuration;

    public ItemOfRecyclerViewHighlighter(final RecyclerView recyclerView,
                                         final PositionOfSettingProvider positionOfSettingProvider,
                                         final Duration highlightDuration) {
        this.recyclerView = recyclerView;
        this.positionOfSettingProvider = positionOfSettingProvider;
        this.highlightDuration = highlightDuration;
    }

    @Override
    public void highlightSetting(final Fragment settingsFragment, final Setting setting) {
        highlightViewAtPosition(positionOfSettingProvider.getPositionOfSetting(setting).orElseThrow());
    }

    private void highlightViewAtPosition(final int position) {
        new Handler().post(
                () -> ViewAtPositionHighlighter.highlightViewAtPosition(recyclerView, position, highlightDuration, () -> {
                }));
    }
}
