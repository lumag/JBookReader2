package lumag.util;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class OrderedSet<E> extends AbstractSet<E> implements Set<E>, IOrderedContainer<E> {

	private class SetIterator implements Iterator<E> {
		private E lastReturnedElement = null;

		public boolean hasNext() {
			return (forwardMap.get(lastReturnedElement) != null);
		}

		public E next() {
			E next = forwardMap.get(lastReturnedElement);
			if (next == null) {
				throw new NoSuchElementException();
			}
			lastReturnedElement = next;
			return lastReturnedElement;
		}

		public void remove() {
			if (lastReturnedElement == null) {
				throw new IllegalStateException();
			}
			E previous = backwardMap.get(lastReturnedElement);
			OrderedSet.this.remove(lastReturnedElement);
			lastReturnedElement = previous;
		}

	}

	private Map<E, E> forwardMap = new HashMap<E, E>();
	private Map<E, E> backwardMap = new HashMap<E, E>();

	public OrderedSet() {
		forwardMap.put(null, null);
		backwardMap.put(null, null);
	}

	@Override
	public Iterator<E> iterator() {
		return new SetIterator();
	}

	@Override
	public int size() {
		return forwardMap.size() - 1;
	}

	@Override
	public boolean add(E e) {
		if (e == null) {
			throw new NullPointerException();
		}
		if (contains(e)) {
			return false;
		}
		E last = backwardMap.get(null);
		forwardMap.put(e, null);
		backwardMap.put(e, last);
		backwardMap.put(null, e);
		forwardMap.put(last, e);

		return true;
	}

	@Override
	public void clear() {
		forwardMap.clear();
		forwardMap.put(null, null);
		backwardMap.clear();
		backwardMap.put(null, null);
	}

	@Override
	public boolean contains(Object o) {
		if (o == null) {
			throw new NullPointerException();
		}
		return forwardMap.containsKey(o);
	}

	@Override
	public boolean remove(Object o) {
		if (o == null) {
			throw new NullPointerException();
		}
		if (!contains(o)) {
			return false;
		}
		E previous = backwardMap.remove(o);
		E next = forwardMap.remove(o);
		forwardMap.put(previous, next);
		backwardMap.put(next, previous);

		return true;
	}

	public E getNext(E element) throws NoSuchElementException {
		if (!contains(element)) {
			throw new NoSuchElementException();
		}
		E result = forwardMap.get(element);
		if (result == null) {
			throw new NoSuchElementException();
		}
		return result;
	}

	public E getPrevious(E element) throws NoSuchElementException {
		if (!contains(element)) {
			throw new NoSuchElementException();
		}
		E result = backwardMap.get(element);
		if (result == null) {
			throw new NoSuchElementException();
		}
		return result;
	}

	public boolean hasNext(E element) {
		if (!contains(element)) {
			throw new NoSuchElementException();
		}
		return forwardMap.get(element) != null;
	}

	public boolean hasPrevious(E element) {
		if (!contains(element)) {
			throw new NoSuchElementException();
		}
		return backwardMap.get(element) != null;
	}

	public int getNumber(E element) throws NoSuchElementException {
		if (!contains(element)) {
			throw new NoSuchElementException();
		}
		
		int num = 0;
		
		for (E temp = element; hasPrevious(temp); temp = getPrevious(temp)) {
			num ++;
		}
		return num;
	}

	public E get(int index) {
		if (index < 0 || index >= size()) {
			throw new NoSuchElementException();
		}
		int i = 0;
		for (E e: this) {
			if (i == index) {
				return e;
			}
			i++;
		}
		throw new InternalError();
	}
}
