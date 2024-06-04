package com.bytehamster.lib.preferencesearch;

import java.util.Objects;

// FK-TODO: remove HistoryItem
class HistoryItem extends ListItem {

    static final int TYPE = 1;
    private final String term;

    HistoryItem(final String term) {
        super();
        this.term = term;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    String getTerm() {
        return term;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final HistoryItem that = (HistoryItem) o;
        return Objects.equals(getTerm(), that.getTerm());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTerm());
    }
}
