package de.KnollFrank.lib.settingssearch.common.compare;

import java.util.Comparator;
import java.util.List;

import de.KnollFrank.lib.settingssearch.common.Lists;

public class LexicographicalListComparator<T> implements Comparator<List<T>> {

	private final Comparator<T> elementComparator;

	public LexicographicalListComparator(final Comparator<T> elementComparator) {
		this.elementComparator = elementComparator;
	}

	@Override
	public int compare(final List<T> list1, final List<T> list2) {
		if (list1.size() < list2.size()) {
			return -1;
		} else if (list1.size() > list2.size()) {
			return +1;
		} else {
			return Lists
					.zip(list1, list2)
					.stream()
					.map(elementPair -> elementComparator.compare(elementPair.first, elementPair.second))
					.filter(compareResult -> compareResult != 0)
					.findFirst()
					.orElse(0);
		}
	}
}
