package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
//import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private boolean full;
    private int updatingSize = 13;
    private int actualSize = 0;

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this.chains = makeArrayOfChains(updatingSize);
        this.full = false;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    @Override
    public V get(K key) {
        if (containsKey(key)) {
            return chains[Math.abs(key.hashCode()) % updatingSize].get(key);	
        }
        throw new NoSuchKeyException(); // Only gets here if no key found
    }

    @Override
    public void put(K key, V value) {
        int index = Math.abs(key.hashCode()) % updatingSize;
        if (full) {
            updatingSize *= 2;
            IDictionary<K, V>[] newChains = makeArrayOfChains(updatingSize);
            for (IDictionary<K, V> item : chains) {
            		if (item != null) {
            			Iterator<KVPair<K, V>> iter = item.iterator();
                     while (iter.hasNext()) {
                        	 KVPair<K, V> newItem = iter.next();
                        	 K newKey = newItem.getKey();
                        	 V newVal = newItem.getValue();
                        	 int newIndex = Math.abs(newKey.hashCode()) % updatingSize;
                        	 if (newChains[newIndex] == null) {
                        		 newChains[newIndex] = new ArrayDictionary<K, V>();
                        	 }
                        	 newChains[newIndex].put(newKey, newVal);
                     }
            		}
            }
            this.chains = newChains;
            full = false;
        }
        if (containsKey(key)) {
            chains[index].put(key, value);
        } else {
            if (chains[index] == null) {
                chains[index] = new ArrayDictionary<K, V>();
            }
            chains[index].put(key, value);
            actualSize++;
            full = actualSize == updatingSize - 1;
        }
    }


    @Override
    public V remove(K key) {
        if (containsKey(key)) {
            int index = Math.abs(key.hashCode()) % updatingSize;
            V value = chains[index].get(key);
            chains[index] = null;
            actualSize--;
            return value;
        }
        throw new NoSuchKeyException();
    }

    @Override
    public boolean containsKey(K key) {
        int index = Math.abs(key.hashCode()) % updatingSize;
        return index > -1 && chains[index] != null && chains[index].containsKey(key);
    }

    @Override
    public int size() {
        return actualSize;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     * 3. Think about what exactly your *invariants* are. An *invariant*
     *    is something that must *always* be true once the constructor is
     *    done setting up the class AND must *always* be true both before and
     *    after you call any method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int index;
        
        public ChainedIterator(final IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < actualSize;
        }

        @Override
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                if (chains[index] != null) {
                    Iterator<KVPair<K, V>> iter = chains[index].iterator();
                    KVPair<K, V> returnValue = iter.next();
                    index++;
                    return returnValue;
                } else {
                    index++;
                    next();
                }
                return null;
            }
        }
    }
    
}
