package nl.han.ica.datastructures;

import java.util.ArrayList;

public class HANLinkedList<T> implements IHANLinkedList<T> {
    public ArrayList<T> list;

    public HANLinkedList() {
        list = new ArrayList<>();
    }

    @Override
    public void addFirst(T value) {
        if(list.size() == 0) {
            list.add(value);
        } else {
            list.add(0, value);
        }
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public void insert(int index, T value) {
        list.add(index, value);
    }

    @Override
    public void delete(int pos) {
        list.remove(pos);
    }

    @Override
    public T get(int pos) {
        return list.get(pos);
    }

    @Override
    public void removeFirst() {
        list.remove(0);
    }

    @Override
    public T getFirst() {
        return list.get(0);
    }

    @Override
    public int getSize() {
        return list.size();
    }
}
