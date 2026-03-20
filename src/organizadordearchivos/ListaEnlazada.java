package organizadordearchivos;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ListaEnlazada implements Iterable<File> {
    
    private FileNode head;
    private int size;
    
    public ListaEnlazada() {
        head = null;
        size = 0;
    }
    
    public void add(File file) {
        FileNode newNode = new FileNode(file);
        if (head == null) {
            head = newNode;
        } else {
            FileNode current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }
    
    public void addAll(List<File> files) {
        for (File f : files) {
            add(f);
        }
    }
    
    public boolean remove(File file) {
        if (head == null) return false;

        if (head.data.equals(file)) {
            head = head.next;
            size--;
            return true;
        }
        
        FileNode current = head;
        while (current.next != null) {
            if (current.next.data.equals(file)) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }
    
    public File get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }
        FileNode current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }
    
    public void clear() {
        head = null;
        size = 0;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int size() {
        return size;
    }
    
    public List<File> toList() {
        List<File> result = new ArrayList<>();
        FileNode current = head;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }
    
    public static ListaEnlazada fromList(List<File> files) {
        ListaEnlazada list = new ListaEnlazada();
        list.addAll(files);
        return list;
    }
    
    @Override
    public Iterator<File> iterator() {
        return new Iterator<File>() {
            private FileNode current = head;
            
            @Override
            public boolean hasNext() {
                return current != null;
            }
            
            @Override
            public File next() {
                if (!hasNext()) throw new NoSuchElementException();
                File data = current.data;
                current = current.next;
                return data;
            }
        };
    }
    
    FileNode getHead() {
        return head;
    }
    
    void addNode(FileNode node) {
        node.next = null;
        if (head == null) {
            head = node;
        } else {
            FileNode current = head;
            while (current.next != null) current = current.next;
            current.next = node;
        }
        size++;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        FileNode current = head;
        while (current != null) {
            sb.append(current.data.getName());
            if (current.next != null) sb.append(", ");
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}