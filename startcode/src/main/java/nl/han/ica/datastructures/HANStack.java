package nl.han.ica.datastructures;

import nl.han.ica.icss.ast.ASTNode;

import java.util.ArrayList;

public class HANStack<T> implements IHANStack<T> {
    private ArrayList<T> stack;

    public HANStack() {
        stack = new ArrayList<T>();
    }

    @Override
    public void push(T value) {
        stack.add(value);
    }

    @Override
    public T pop() {
        if (stack.isEmpty()) {
            return null;
        }
        T tmp = stack.get( stack.size()-1 );
        stack.remove(stack.size()-1);
        return tmp;
    }

    @Override
    public T peek() {
        if (stack.isEmpty()) {
            return null;
        }
        return stack.get(stack.size()-1);
    }

    public ArrayList<T> getStack() {
        return stack;
    }
}
