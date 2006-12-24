package lumag.util;

import java.util.NoSuchElementException;

public interface IOrderedContainer<E> {
	boolean contains(E element);
	boolean hasNext(E element) throws NoSuchElementException;
	boolean hasPrevious(E element) throws NoSuchElementException;
	E getNext(E element) throws NoSuchElementException;
	E getPrevious(E element) throws NoSuchElementException;
}
