import java.util.ArrayList;
import java.util.EmptyStackException;

// TODO: Auto-generated Javadoc
/**
 * The Class GenericStack. Implements a generic software stack for any element
 * IMPORTANT: You need to replace with YOUR GenericStack Implementation!!!
 *
 *
 * @param <E> the element type
 */
public class GenericStack<E>  {
	
	/** The stack.  The stack will be built on a generic ArrayList, but will only
	 *  expose stack methods push, pop, peek, isEmpty and getSize.
	 */
	private ArrayList<E> stack;
	
	/**
	 * Instantiates a new generic stack. The stack is empty at the beginning
	 */
	public GenericStack() {
		stack = new ArrayList<>();
	}
	
	public boolean empty() {
		if(stack.isEmpty()) {
			return true;
		}
		return false;	
	}

	/**
	 * Gets the size of the stack. The Top of Stack is size - 1;
	 *
	 * @return the size
	 */
	public int size() {
		return stack.size();
	}

	/**
	 * Peek - this is a Java stack function that returns the object at the
	 * top of the stack without removing it from the stack.
	 *
	 * @return the object at the top of the stack
	 * @throws EmptyStackException if an attempt was made to peek on an empty stack
	 */
	public E peek() {
		if(empty()) {
			throw new EmptyStackException();
		}
		return stack.get(size() - 1);
	}

	/**
	 * Pops the object off of the top of the stack, and returns it. The ArrayList
	 * remove method is used to implement this.
	 *
	 * @return the object at the top of the stack
	 * @throws EmptyStackException if an attempt was made to pop on an empty stack
	 */
	public E pop() {
		if(empty()) {
			throw new EmptyStackException();
		}
		
		E top = peek();
		stack.remove(size() - 1);
		return top;
	}
	
	/**
	 * Pushes an object onto the stack using the ArrayList add method. This also
	 * adjusts the size of the stack directly...
	 *
	 * @param o the object to be added to the stack
	 */
	public void push(E o) {
		stack.add(o);
	};

	/**
	 * To string
	 *
	 * @return the string
	 */
	@Override
   	public String toString() {
	   return("stack: "+stack.toString());
	}
	

}
