import java.util.ArrayDeque;

/**
 *  @(#)ArrayStack<E>.java
 *
 * This class provides a FILO stack for the elements stored in it.
 * It is an adapter from the standard generic Java ArrayDeque class
 * to provide the methods for the FILO stack.
 *
 *  Dependencies: ArrayDeque.java
 *
 *	@author Ali Mostafa Almousa
 *  @version 3.10 2022/2/22
 *
 * @param <E> the type of elements held in this stack
 */
public class ArrayStack<E> extends ArrayDeque<E> {
	private int capacity;				// Impose a limit on the stack size
	
	public ArrayStack() {				// The default constructor
		super();
		this.capacity = 16;				// Default size limit is 16
	}
	
	public ArrayStack(int capacity) {	// Parameterized constructor
		super(capacity);
		this.capacity = capacity;		// The stack has the given size limit
	}	
	
	public void top(E ele) {			// Adaptor method for adding one element
		push(ele);
	}
	
	public E retrieve() {				// Adaptor method for removing one element
		return pop();
	}
	
	public E glimpse() {				// Adaptor method for peeking one element
		return peek();
	}
	
	public boolean isFull() {			// A new method to test if the stack is full
		return size() == this.capacity;
	}

}
