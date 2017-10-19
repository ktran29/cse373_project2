/**
 * CSE 373
 * Project 1 - Part 1
 * ArrayDictionary class
 *
 * Marcus Deichman and Kevin Tran
 */

package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
//import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    /**
     * Adds the given item to the *end* of this IList.
     */
    @Override
    public void add(T item) {
        if (front == null && back == null) {
            Node<T> newNode = new Node<T>(item);
            front = newNode;
            back = newNode;
        } else {
            Node<T> newNode = new Node<T>(back, item, null);
            back.next = newNode;
            back = newNode;
        }
        size++;
    }

    /**
     * Removes and returns the item from the *end* of this IList.
     *
     * @throws EmptyContainerException if the container is empty and there is no element to remove.
     */
    @Override
    public T remove() {
        if (size == 0) {
            throw new EmptyContainerException();
        } else if (front != null) {
            T returnedValue = back.data;
            back = back.prev;
            size--;
            if (size == 0) {
            	front = null;
            }
            return returnedValue;
        } else {
            return null;
        }
    }

    /**
     * Returns the item located at the given index.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
    @Override
    public T get(int index) {
		if (index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException();
		}
        if (front != null) {
            Node<T> temp = front;
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
            return temp.data;
        }
        return null;
    }

    /**
     * Overwrites the element located at the given index with the new item.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
    @Override
    public void set(int index, T item) {
        if (index > size - 1 || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> temp = front;
        for (int i = 0; i < index; i++) {
        	if (temp.next != null) {
        		temp = temp.next;
        	}
        }
        Node<T> newEntry = new Node<T>(temp.prev, item, temp.next);
        if (temp.prev != null) {
        	temp.prev.next = newEntry;
        } else {
        	front = newEntry;
        }
        if (temp.next != null) {
        	temp.next.prev = newEntry;
        } else {
        	back = newEntry;
        }
    }

    /**
     * Inserts the given item at the given index. If there already exists an element
     * at that index, shift over that element and any subsequent elements one index
     * higher.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size() + 1
     */
    @Override
    public void insert(int index, T item) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		Node<T> temp = front;
	    if (index == 0) {
	        Node<T> newEntry = new Node<T>(null, item, front);
	        front = newEntry;
	        if (size == 0) {
	            back = newEntry;
	        }
	    } else if (index == size) {
	        Node<T> newEntry = new Node<T>(back, item, null);
	        if(back != null) {
	        	back.next = newEntry;
	        }
	        back = newEntry;
		} else if (index > size / 2) {
			temp = back;
			for (int i = size - 1; i > index; i--) {
	            temp = temp.prev;
	        }
	        Node<T> newEntry = new Node<T>(temp.prev, item, temp);
	        temp.prev = newEntry;
		} else {
	        for (int i = 0; i < index - 1; i++) {
	            temp = temp.next;
	        }
	        Node<T> newEntry = new Node<T>(temp, item, temp.next);
	        temp.next = newEntry;
	    }
	    size++;
    }

    /**
     * Deletes the item at the given index. If there are any elements located at a higher
     * index, shift them all down by one.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
    @Override
    public T delete(int index) {
        Node<T> temp = front;
        T returnedValue = null;
        if (index < 0 || index > size - 1) {
        		throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
        		returnedValue = front.data;
        		if (front.next == null) {
        			front = null;
        			back = null;
        		} else {
        			front.next.prev = null;
        			front = front.next;
        		}
        		size--;
        		temp = null;
        }
        if (temp != null) {
        		for (int i = 0; i < index; i++) {
        			temp = temp.next;
        		}
        		if (temp != null) {
        			returnedValue = temp.data;
        			if (temp.next == null) {
        				temp.prev.next = null;
        			} else {
        				temp.prev.next = temp.next;
        				temp.next.prev = temp.prev;
        			}
                    size--;
        		}
        }
        return returnedValue;
    }

    /**
     * Returns the index corresponding to the first occurrence of the given item
     * in the list.
     *
     * If the item does not exist in the list, return -1.
     */
    @Override
    public int indexOf(T item) {
        if (front != null) {
            int index = 0;
            Node<T> temp = front;
            while (temp != null) {
                if (temp.data == item || temp.data.equals(item)) {
                    return index;
                } else {
                	index++;
                	temp = temp.next;
                }
            }
        }
        return -1;

    }

    /**
     * Returns the number of elements in the container.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns 'true' if this container contains the given element, and 'false' otherwise.
     */
    @Override
    public boolean contains(T other) {
        Node<T> temp = front;
        while (temp != null) {
        	if (temp.data == other || temp.data.equals(other)) {
        		return true;
        	}
        	temp = temp.next;
        }
        return false;
    }

    /**
     * Returns an iterator over the contents of this list.
     */
    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
            	T returnedValue = current.data;
                current = current.next;
                return returnedValue;
            }
        }
    }
}
