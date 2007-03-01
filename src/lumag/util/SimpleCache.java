package lumag.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SimpleCache<K,V> {
	public interface Getter<K,V> {
		V process(K key);
	}
	
	private final Getter<K,V> getter;
	
	private final ReferenceQueue<V> queue = new ReferenceQueue<V>();
	private final Map<K, Reference<V>> forwardMap = new HashMap<K, Reference<V>>();
	private final Map<Reference<V>, K> backwardMap = new HashMap<Reference<V>, K>();
	
	public SimpleCache(final Getter<K, V> getter) {
		this.getter = getter;
	}

	public V get(K key) {
		clearQueue();

		V value = null;

		Reference<V> ref = forwardMap.get(key);
		if (ref != null) {
			value = ref.get();
			if (value == null) {
				backwardMap.remove(ref);
				forwardMap.remove(key);
			}
		}
		
		if (value == null) {
			value = addValue(key);
		}

		return value;
	}
	
	public void remove(K key) {
		Reference<V> ref = forwardMap.remove(key);
		backwardMap.remove(ref);
		ref.clear();
		clearQueue();
	}
	
	public void clear() {
		for (Iterator<Reference<V>> it = backwardMap.keySet().iterator();
			it.hasNext();
			) {
			it.next().clear();
			it.remove();
		}
		forwardMap.clear();
		while (queue.poll() != null) {
			// do nothing
		}
	}

	private void clearQueue() {
		Reference<? extends V> ref;
		while ((ref = queue.poll()) != null) {
			K key = backwardMap.remove(ref);
			forwardMap.remove(key);
		}
	}

	private V addValue(K key) {
		V value = this.getter.process(key);

		Reference<V> ref = new SoftReference<V>(value, queue);

		forwardMap.put(key, ref);
		backwardMap.put(ref, key);

		return value;
	}
}
