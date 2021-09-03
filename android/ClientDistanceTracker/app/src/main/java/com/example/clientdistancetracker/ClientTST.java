package com.example.clientdistancetracker;

//import java.util.Queue;

import java.util.ArrayList;

public class ClientTST {
    private int n;              // size
    private Node root;   // root of TST
    private Double total_distance = 0.0;  //total distance of all current clients in TST

    private static class Node {
        private char c;                        // character
        private Node left, mid, right;  // left, middle, and right subtries
        private Double distance;                     // distance between clients
        private Integer visits = 0;           //number of times clients were visited
    }

    /**
     * Initializes an empty string symbol table.
     */
    public ClientTST() {
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    public Double total_distance() {
        return total_distance;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Double get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node x = get(root, key, 0);
        if (x == null) return null;
        return x.distance;  //returns the value associated with the key
    }

    // return subtrie corresponding to given key
    private Node get(Node x, String key, int d) {
        if (x == null) return null;   //if a node is ever null, it does not exist in the TST
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        char c = key.charAt(d);  //move through word
        if      (c < x.c)              return get(x.left,  key, d);  //if the current char is less than the char at the current node, check to the left of current node
        else if (c > x.c)              return get(x.right, key, d);  //if the current char is more than the char at the current node, check to the right of current node
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);  //if current char == char at node, then move to the next node and next character in the word
        else                           return x;  //if we have read through the whole word, return the final node.
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key, Double val) {   //to delete, make val == null
        if (key == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }
        if (val != null && contains(key) && get(key) != val) val = get(key);  // if name is already there, it makes no sense for distance to change
        if (!contains(key)) n++;    //if it does not contain the key, then the tree grows
        else if(val == null) n--;       // delete existing key by making its value null
        root = put(root, key, val, 0);  //start adding new word from root
    }

    private Node put(Node x, String key, Double val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
            //System.out.println(c);
            //System.out.println(d);
        }
        if      (c < x.c)               x.left  = put(x.left,  key, val, d);
        else if (c > x.c)               x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1);
        else {
            if (val == null){   //deleting node, remove distance added and reset visits on node to 0
                total_distance = total_distance - (x.distance*x.visits);
                x.visits = 0;
            }
            else{
                x.visits++;
                total_distance = total_distance + val;
            }

            x.distance   = val;   //mark final node of word


        }
        return x;
    }

    public void put(String key, Double val, Integer visits) {   //to delete, make val == null
        if (key == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }
        if (val != null && contains(key) && get(key) != val) val = get(key);  // if name is already there, it makes no sense for distance to change
        if (!contains(key)) n++;    //if it does not contain the key, then the tree grows
        else if(val == null) n--;       // delete existing key by making its value null
        root = put(root, key, val, 0, visits);  //start adding new word from root
    }

    private Node put(Node x, String key, Double val, int d, Integer visits) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
            //System.out.println(c);
            //System.out.println(d);
        }
        if      (c < x.c)               x.left  = put(x.left,  key, val, d, visits);
        else if (c > x.c)               x.right = put(x.right, key, val, d, visits);
        else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1, visits);
        else {
            if (val == null){   //deleting node, remove distance added and reset visits on node to 0
                total_distance = total_distance - (x.distance*x.visits);
                x.visits = 0;
            }
            else{
                Integer old = x.visits;
                x.visits = visits;
                total_distance = total_distance + (x.visits - old)*val;
            }

            x.distance   = val;   //mark final node of word


        }
        return x;
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null}, if no such string.
     * @param query the query string
     * @return the string in the symbol table that is the longest prefix of {@code query},
     *     or {@code null} if no such string
     * @throws IllegalArgumentException if {@code query} is {@code null}
     */
    public String longestPrefixOf(String query) {   //Not really needed.......
        if (query == null) {
            throw new IllegalArgumentException("calls longestPrefixOf() with null argument");
        }
        if (query.length() == 0) return null;
        int length = 0;
        Node x = root;
        int i = 0;
        while (x != null && i < query.length()) {
            char c = query.charAt(i);
            if      (c < x.c) x = x.left;
            else if (c > x.c) x = x.right;
            else {
                i++;
                if (x.distance != null) length = i;
                x = x.mid;
            }
        }
        return query.substring(0, length);
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public ArrayList<String[]> keys() {
        ArrayList<String[]> clients = new ArrayList<String[]>();
        collect(root, new StringBuilder(), clients);
        return clients;
    }

    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     *     as an iterable
     * @throws IllegalArgumentException if {@code prefix} is {@code null}
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
        Queue<String> queue = new Queue<String>();
        Node x = get(root, prefix, 0);
        if (x == null) return queue;
        if (x.distance != null) queue.enqueue(prefix);
        collect(x.mid, new StringBuilder(prefix), queue);
        return queue;
    }

    // all keys in subtrie rooted at x with given prefix
    private void collect(Node x, StringBuilder prefix, ArrayList<String[]> arr) {
        if (x == null) return;
        collect(x.left,  prefix, arr);
        if (x.distance != null) {
            String[] info = new String[]{prefix.toString() + x.c, String.valueOf(x.visits), String.valueOf(x.distance)};
            arr.add(info);
        }
        collect(x.mid,   prefix.append(x.c), arr);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, arr);
    }

    // all keys in subtrie rooted at x with given prefix
    private void collect(Node x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.left,  prefix, queue);
        if (x.distance != null) queue.enqueue(prefix.toString() + x.c);
        collect(x.mid,   prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, queue);
    }


    /**
     * Returns all of the keys in the symbol table that match {@code pattern},
     * where the character '.' is interpreted as a wildcard character.
     * @param pattern the pattern
     * @return all of the keys in the symbol table that match {@code pattern},
     *     as an iterable, where . is treated as a wildcard character.
     */
    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> queue = new Queue<String>();
        collect(root, new StringBuilder(), 0, pattern, queue);
        return queue;
    }

    private void collect(Node x, StringBuilder prefix, int i, String pattern, Queue<String> queue) {
        if (x == null) return;
        char c = pattern.charAt(i);
        if (c == '.' || c < x.c) collect(x.left, prefix, i, pattern, queue);
        if (c == '.' || c == x.c) {
            if (i == pattern.length() - 1 && x.distance != null) queue.enqueue(prefix.toString() + x.c);
            if (i < pattern.length() - 1) {
                collect(x.mid, prefix.append(x.c), i+1, pattern, queue);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        if (c == '.' || c > x.c) collect(x.right, prefix, i, pattern, queue);
    }
}
