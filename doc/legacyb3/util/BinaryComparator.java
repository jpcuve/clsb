package legacyb3.util;

import java.util.Comparator;

public interface BinaryComparator extends Comparator {
	public long evaluate(Object o1, Object o2);
}
