package lk.uom.advanced_algorithms.provided_sources;

import lk.uom.advanced_algorithms.Util;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

/******************************************************************************
 *  Compilation:  javac SplayBST.java
 *  Execution:    java SplayBST
 *  Dependencies: none
 *
 *  Splay tree. Supports splay-insert, -search, and -delete.
 *  Splays on every operation, regardless of the presence of the associated
 *  key prior to that operation.
 *
 *  Written by Josh Israel.
 *
 *  Modified by Sanath Jayasena
 *
 ******************************************************************************/


public class SplayBST<Key extends Comparable<Key>, Value> {

    private Node root;   // root of the BST

    // BST helper node data type
    private class Node {
        private Key key;            // key
        private Value value;        // associated data
        private Node left, right;   // left and right subtrees

        public Node(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
    }

    public boolean contains(Key key) {
        return get(key) != null;
    }

    // return value associated with the given key
    // if no such value, return null
    public Value get(Key key) {
        root = splay(root, key);
        int cmp = key.compareTo(root.key);
        if (cmp == 0) return root.value;
        else return null;
    }

    /***************************************************************************
     *  Splay tree insertion.
     ***************************************************************************/
    public void put(Key key, Value value) {
        // splay key to root
        if (root == null) {
            root = new Node(key, value);
            return;
        }

        root = splay(root, key);

        int cmp = key.compareTo(root.key);

        // Insert new node at root
        if (cmp < 0) {
            Node n = new Node(key, value);
            n.left = root.left;
            n.right = root;
            root.left = null;
            root = n;
        }

        // Insert new node at root
        else if (cmp > 0) {
            Node n = new Node(key, value);
            n.right = root.right;
            n.left = root;
            root.right = null;
            root = n;
        }

        // It was a duplicate key. Simply replace the value
        else {
            root.value = value;
        }

    }

    /***************************************************************************
     *  Splay tree deletion.
     ***************************************************************************/
    /* This splays the key, then does a slightly modified Hibbard deletion on
     * the root (if it is the node to be deleted; if it is not, the key was
     * not in the tree). The modification is that rather than swapping the
     * root (call it node A) with its successor, it's successor (call it Node B)
     * is moved to the root position by splaying for the deletion key in A's
     * right subtree. Finally, A's right child is made the new root's right
     * child.
     */
    public void remove(Key key) {
        if (root == null) return; // empty tree

        root = splay(root, key);

        int cmp = key.compareTo(root.key);

        if (cmp == 0) {
            if (root.left == null) {
                root = root.right;
            } else {
                Node x = root.right;
                root = root.left;
                splay(root, key);
                root.right = x;
            }
        }

        // else: it wasn't in the tree to remove
    }


    /***************************************************************************
     * Splay tree function.
     * **********************************************************************/
    // splay key in the tree rooted at Node h. If a node with that key exists,
    //   it is splayed to the root of the tree. If it does not, the last node
    //   along the search path for the key is splayed to the root.
    private Node splay(Node h, Key key) {
        if (h == null) return null;

        int cmp1 = key.compareTo(h.key);

        if (cmp1 < 0) {
            // key not in tree, so we're done
            if (h.left == null) {
                return h;
            }
            int cmp2 = key.compareTo(h.left.key);
            if (cmp2 < 0) {
                h.left.left = splay(h.left.left, key);
                h = rotateRight(h);
            } else if (cmp2 > 0) {
                h.left.right = splay(h.left.right, key);
                if (h.left.right != null)
                    h.left = rotateLeft(h.left);
            }

            if (h.left == null) return h;
            else return rotateRight(h);
        } else if (cmp1 > 0) {
            // key not in tree, so we're done
            if (h.right == null) {
                return h;
            }

            int cmp2 = key.compareTo(h.right.key);
            if (cmp2 < 0) {
                h.right.left = splay(h.right.left, key);
                if (h.right.left != null)
                    h.right = rotateRight(h.right);
            } else if (cmp2 > 0) {
                h.right.right = splay(h.right.right, key);
                h = rotateLeft(h);
            }

            if (h.right == null) return h;
            else return rotateLeft(h);
        } else return h;
    }


    /***************************************************************************
     *  Helper functions.
     ***************************************************************************/

    // height of tree (1-node tree has height 0)
    public int height() {
        return height(root);
    }

    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }


    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return 1 + size(x.left) + size(x.right);
    }

    // right rotate
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        return x;
    }

    // left rotate
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        return x;
    }

    @SneakyThrows
    public static void main(String[] args) {

        // INSERT ----------------------------------------------------

        // // for visual graphs
        // System.out.println("Splay BST init ...");
        // Thread.sleep(10 * 1_000);
        // System.out.println("Splay BST ready ...");
        //
        // Path insert = Path.of("data", "insert");
        //
        // for (int _set = 1; _set <= 2; _set++) {
        //     for (int _data = 1; _data <= 3; _data++) {
        //         String set = "set" + _set;
        //         String data = "data_" + _data + ".txt";
        //         Path actual = insert.resolve(set).resolve(data);
        //
        //         List<Long> numbers = Util.loadData(actual);
        //         SplayBST<Long, Long> st = new SplayBST<>();
        //         long timeTaken = insert(numbers, st);
        //         System.out.printf("Time taken to 'insert' %d numbers (%s : %s) into a 'Splay BST' took = %d micro seconds\n",
        //                 numbers.size(), set, data, timeTaken);
        //     }
        // }

        // END INSERT ----------------------------------------------------


        // SEARCH --------------------------------------------------------

        // // for visual graphs
        // System.out.println("Splay BST init ...");
        // Thread.sleep(15 * 1_000);
        // System.out.println("Splay BST ready ...");
        //
        // Path _insert = Path.of("data", "insert");
        // Path _search = Path.of("data", "search");
        //
        // for (int _set = 1; _set <= 2; _set++) {
        //     for (int _data = 1; _data <= 3; _data++) {
        //         String set = "set" + _set;
        //         String data = "data_" + _data + ".txt";
        //         Path insert = _insert.resolve(set).resolve(data);
        //         Path search = _search.resolve(set).resolve(data);
        //
        //         List<Long> insertNumbers = Util.loadData(insert);
        //         List<Long> searchNumbers = Util.loadData(search);
        //         SplayBST<Long, Long> st = new SplayBST<>();
        //         insert(insertNumbers, st);
        //         long timeTaken = search(searchNumbers, st);
        //         System.out.printf("Time taken to 'search' (%d / %d) numbers (%s : %s) into a 'Splay BST' took = %d micro seconds\n",
        //                 searchNumbers.size(), insertNumbers.size(), set, data, timeTaken);
        //     }
        // }

        // END SEARCH --------------------------------------------------------


        // DELETE ------------------------------------------------------------

        // for visual graphs
        System.out.println("Splay BST init ...");
        Thread.sleep(15 * 1_000);
        System.out.println("Splay BST ready ...");

        Path _insert = Path.of("data", "insert");
        Path _delete = Path.of("data", "delete");

        for (int _set = 1; _set <= 2; _set++) {
            for (int _data = 1; _data <= 3; _data++) {
                String set = "set" + _set;
                String data = "data_" + _data + ".txt";
                Path insert = _insert.resolve(set).resolve(data);
                Path delete = _delete.resolve(set).resolve(data);

                List<Long> insertNumbers = Util.loadData(insert);
                List<Long> deleteNumbers = Util.loadData(delete);
                SplayBST<Long, Long> st = new SplayBST<>();
                insert(insertNumbers, st);
                long timeTaken = delete(deleteNumbers, st);
                System.out.printf("Time taken to 'delete' (%d / %d) numbers (%s : %s) into a 'Splay BST' took = %d micro seconds\n",
                        deleteNumbers.size(), insertNumbers.size(), set, data, timeTaken);
            }
        }

        // END DELETE --------------------------------------------------------
    }

    private static long insert(List<Long> numbers, SplayBST<Long, Long> corpus) {
        var start = LocalDateTime.now();
        for (Long number : numbers) {
            corpus.put(number, number);
        }
        var end = LocalDateTime.now();
        return Util.duration(start, end);
    }

    private static long search(List<Long> numbers, SplayBST<Long, Long> corpus) {
        var start = LocalDateTime.now();
        for (Long number : numbers) {
            corpus.get(number);
        }
        var end = LocalDateTime.now();
        return Util.duration(start, end);
    }

    private static long delete(List<Long> numbers, SplayBST<Long, Long> corpus) {
        var start = LocalDateTime.now();
        for (Long number : numbers) {
            corpus.remove(number);
        }
        var end = LocalDateTime.now();
        return Util.duration(start, end);
    }
}