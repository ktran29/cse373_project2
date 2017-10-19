/**
 * CSE 373
 * Project 1 - Part 1
 * ArrayDictionary class
 * 
 * Marcus Deichman and Kevin Tran
 */

package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;

    // You're encouraged to add extra fields (and helper methods) though!

    public ArrayDictionary() {
    	this.pairs = makeArrayOfPairs(4);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }

    /**
     * Returns the value corresponding to the given key.
     *
     * @throws NoSuchKeyException if the dictionary does not contain the given key.
     */
    @Override
    public V get(K key) {
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i] != null && (pairs[i].key == key || pairs[i].key.equals(key))) {
                return pairs[i].value;
            }
        }
        throw new NoSuchKeyException(); // Only gets here if no key found
    }

    /**
     * Adds the key-value pair to the dictionary. If the key already exists in the dictionary,
     * replace its value with the given one.
     */
    @Override
    public void put(K key, V value) {
        int index = 0;
        boolean foundKey = false;
        for (int i = 0; i < pairs.length; i++) {
        	if (pairs[i] == null) {
            	index = i - 1;
            	i = pairs.length;
            }
        	else if (pairs[i].key == key || pairs[i].key.equals(key) ) {
                foundKey = true;
                index = i - 1;
                i = pairs.length;
            }
            index++;
        }
        if (foundKey) {
            pairs[index].value = value;
        } else if (index >= pairs.length) {
        	Pair<K, V>[] newPairs = makeArrayOfPairs(pairs.length * 2);
        	for (int i = 0; i < pairs.length; i++) {
        		newPairs[i] = pairs[i];
        	}
        	pairs = newPairs;
        	pairs[index] = new Pair<K, V>(key, value);
        } else {
        	pairs[index] = new Pair<K, V>(key, value);
        }
    }

    /**
     * Remove the key-value pair corresponding to the given key from the dictionary.
     *
     * @throws NoSuchKeyException if the dictionary does not contain the given key.
     */
    @Override
    public V remove(K key) {
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i] != null && (pairs[i].key == key || pairs[i].key.equals(key))) {
                V removedValue = pairs[i].value;
                pairs[i] = null;
                for (int j = i; j < pairs.length - 1; j++) {
                	pairs[j] = pairs[j + 1];
                }
                pairs[pairs.length - 1] = null;
                return removedValue;
            }
        }
        throw new NoSuchKeyException();
    }

    /**
     * Returns 'true' if the dictionary contains the given key and 'false' otherwise.
     */
    @Override
    public boolean containsKey(K key) {
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i] != null && (pairs[i].key == key || pairs[i].key.equals(key))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the number of key-value pairs stored in this dictionary.
     */
    @Override
    public int size() {
    	int pairCount = 0;
    	for (int i = 0; i < pairs.length; i++) {
    		if (pairs[i] != null) {
    			pairCount++;
    		} else {
    			i = pairs.length;
    		}
    	}
        return pairCount;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
