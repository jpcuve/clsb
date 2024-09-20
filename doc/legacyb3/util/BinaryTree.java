package legacyb3.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: 4/13/11
 * Time: 11:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class BinaryTree extends TreeSet {
    public BinaryTree() {
    }

    public BinaryTree(Comparator comparator) {
        super(comparator);
    }

    public BinaryTree(Collection c) {
        super(c);
    }

    public BinaryTree(SortedSet s) {
        super(s);
    }
}
