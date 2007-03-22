package lumag.util;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RevertIterable<T> implements Iterable<T> {
	
	public class RevertIterator implements Iterator<T> {
		private ListIterator<T> it = list.listIterator(list.size());

		public boolean hasNext() {
			return it.hasPrevious();
		}

		public T next() {
			return it.previous();
		}

		public void remove() {
			it.remove();
		}

	}

	private final List<T> list;

	public RevertIterable(final List<T> list) {
		this.list = list;
	}

	public Iterator<T> iterator() {
		return new RevertIterator();
	}

}
