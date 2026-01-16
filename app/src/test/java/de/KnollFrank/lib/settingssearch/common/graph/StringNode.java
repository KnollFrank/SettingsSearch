package de.KnollFrank.lib.settingssearch.common.graph;

import java.util.Objects;

class StringNode {

    private final String label;

    public StringNode(final String label) {
        this.label = Objects.requireNonNull(label, "Vertex label cannot be null");
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final StringNode that = (StringNode) o;
        return label.equals(that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }

    @Override
    public String toString() {
        return label;
    }
}
