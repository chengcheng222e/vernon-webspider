/**
 * 
 */
package com.vernon.webspider.core.collection;

import java.io.Serializable;

/**
 * 实现一对简单的对象包裹
 * 
 * @author Vernon.Chen
 *
 * @param <K>
 * @param <V>
 */
@SuppressWarnings("serial")
public class Pair<K, V>
	implements Serializable {

	private K key;

	private V value;

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * @return the key
	 */
	public final K getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public final void setKey(K key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public final V getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public final void setValue(V value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Pair [key=" + key + ", value=" + value + "]";
	}
}
