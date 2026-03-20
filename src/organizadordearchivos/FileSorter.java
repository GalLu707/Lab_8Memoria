package organizadordearchivos;

import java.io.File;
import java.util.Comparator;

public class FileSorter {

    public static ListaEnlazada bubbleSort(ListaEnlazada list, Comparator<File> comparator) {

        ListaEnlazada sorted = copyList(list);
        
        if (sorted.size() <= 1) return sorted;
        
        boolean swapped;
        
        do {
            swapped = false;
            FileNode current = sorted.getHead();

            while (current != null && current.next != null) {
                if (comparator.compare(current.data, current.next.data) > 0) {

                    File temp = current.data;
                    current.data = current.next.data;
                    current.next.data = temp;
                    swapped = true;
                }
                current = current.next;
            }
        } while (swapped);

        return sorted;
    }

    public static ListaEnlazada mergeSort(ListaEnlazada list,
                                           Comparator<File> comparator) {
        ListaEnlazada copy = copyList(list);
        ListaEnlazada sortedHead = mergeSortNode(copy.getHead(), comparator);

        ListaEnlazada result = new ListaEnlazada();
        FileNode current = sortedHead;
        while (current != null) {
            result.addNode(new FileNode(current.data));
            current = current.next;
        }
        return result;
    }

    private static FileNode mergeSortNode(FileNode head, Comparator<File> cmp) {
        if (head == null || head.next == null) return head;

        FileNode mid = getMiddle(head);
        FileNode secondHalf = mid.next;
        mid.next = null;

        FileNode left  = mergeSortNode(head, cmp);
        FileNode right = mergeSortNode(secondHalf, cmp);

        return merge(left, right, cmp);
    }

    private static FileNode merge(FileNode left, FileNode right, Comparator<File> cmp) {

        FileNode dummy = new FileNode(null);
        FileNode tail  = dummy;

        while (left != null && right != null) {
            if (cmp.compare(left.data, right.data) <= 0) {
                tail.next = left;
                left = left.next;
            } else {
                tail.next = right;
                right = right.next;
            }
            tail = tail.next;
        }

        tail.next = (left != null) ? left : right;
        return dummy.next;
    }

    private static FileNode getMiddle(FileNode head) {
        if (head == null) return null;

        FileNode slow = head;
        FileNode fast = head.next;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
    
    private static ListaEnlazada copyList(ListaEnlazada original) {
        ListaEnlazada copy = new ListaEnlazada();
        for (File f : original) {
            copy.add(f);
        }
        return copy;
    }
}
