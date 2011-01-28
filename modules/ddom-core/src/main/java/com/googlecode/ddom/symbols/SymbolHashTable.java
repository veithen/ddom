/*
 * Copyright 2009-2011 Andreas Veithen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.ddom.symbols;


/**
 * Symbol table implementation based on a hash table data structure. The implementation has the
 * following characteristics:
 * <ul>
 * <li>The entries in a bucket are stored using a linked list. Each entry stores the String object
 * for the symbol, as well as the full hash of the symbol. Thus, an entry is created for each symbol
 * added to the table, not only in the case of collision.
 * <li>The hash is computed using the algorithm implemented by the {@link String} class. Wherever
 * possible, {@link String#hashCode()} is used so that the hash value cached in the {@link String}
 * instance can be used.
 * </ul>
 * This class is not thread safe.
 * 
 * @author Andreas Veithen
 */
public class SymbolHashTable implements Symbols {
    static class Symbol {
        String str;
        int hash;
        Symbol next;
    }
    
    private final float loadFactor;
    private Symbol[] buckets;
    private int mask;
    private int size;
    private int threshold;
    
    public SymbolHashTable() {
        this(128, 0.75f);
    }
    
    public SymbolHashTable(int capacity, float loadFactor) {
        this.loadFactor = loadFactor;
        int cap = 1;
        while (cap < capacity) { 
            cap <<= 1;
        }
        buckets = new Symbol[cap];
        mask = cap-1;
        threshold = (int)(cap * loadFactor);
    }

    private void increaseCapacity() {
        int capacity = buckets.length;
        int newCapacity = capacity*2;
        int newMask = newCapacity-1;
        Symbol[] newBuckets = new Symbol[newCapacity];
        for (int i=0; i<capacity; i++) {
            Symbol symbol = buckets[i];
            while (symbol != null) {
                Symbol next = symbol.next;
                int index = symbol.hash & newMask;
                symbol.next = newBuckets[index];
                newBuckets[index] = symbol;
                symbol = next;
            }
        }
        buckets = newBuckets;
        mask = newMask;
        threshold = (int)(newCapacity * loadFactor);
    }
    
    public String lookupSymbol(String str) {
        return lookupSymbol(str, str.hashCode());
    }
    
    private String lookupSymbol(String str, int hash) {
        Symbol symbol = buckets[hash & mask];
        while (symbol != null) {
            if (symbol.hash == hash) {
                String symStr = symbol.str;
                if (symStr.equals(str)) {
                    return symStr;
                }
            }
            symbol = symbol.next;
        }
        return null;
    }

    public String lookupSymbol(String str, int beginIndex, int endIndex) {
        return lookupSymbol(str, beginIndex, endIndex, computeHash(str, beginIndex, endIndex));
    }
    
    private int computeHash(String str, int beginIndex, int endIndex) {
        int hash = 0;
        for (int i=beginIndex; i<endIndex; i++) {
            hash = 31*hash + str.charAt(i);
        }
        return hash;
    }
    
    private String lookupSymbol(String str, int beginIndex, int endIndex, int hash) {
        Symbol symbol = buckets[hash & mask];
        int len = endIndex - beginIndex;
        while (symbol != null) {
            if (symbol.hash == hash) {
                String symStr = symbol.str;
                if (symStr.length() == len) {
                    boolean equal = true;
                    for (int i=0; i<len; i++) {
                        if (symStr.charAt(i) != str.charAt(beginIndex+i)) {
                            equal = false;
                            break;
                        }
                    }
                    if (equal) {
                        return symStr;
                    }
                }
            }
            symbol = symbol.next;
        }
        return null;
    }

    public String lookupSymbol(char[] str, int offset, int length) {
        return lookupSymbol(str, offset, length, computeHash(str, offset, length));
    }
    
    private int computeHash(char[] str, int offset, int length) {
        int hash = 0;
        for (int i=0; i<length; i++) {
            hash = 31*hash + str[offset+i];
        }
        return hash;
    }
    
    private String lookupSymbol(char[] str, int offset, int length, int hash) {
        Symbol symbol = buckets[hash & mask];
        while (symbol != null) {
            if (symbol.hash == hash) {
                String symStr = symbol.str;
                if (symStr.length() == length) {
                    boolean equal = true;
                    for (int i=0; i<length; i++) {
                        if (symStr.charAt(i) != str[offset+i]) {
                            equal = false;
                            break;
                        }
                    }
                    if (equal) {
                        return symStr;
                    }
                }
            }
            symbol = symbol.next;
        }
        return null;
    }
    
    private void storeSymbol(String str, int hash) {
        if (size++ == threshold) {
            increaseCapacity();
        }
        int index = hash & mask;
        Symbol symbol = new Symbol();
        symbol.str = str;
        symbol.hash = hash;
        symbol.next = buckets[index];
        buckets[index] = symbol;
    }
    
    public String getSymbol(String str) {
        int hash = str.hashCode();
        String symbol = lookupSymbol(str, hash);
        if (symbol != null) {
            return symbol;
        } else {
            storeSymbol(str, hash);
            return str;
        }
    }

    public String getSymbol(String str, int beginIndex, int endIndex) {
        int hash = computeHash(str, beginIndex, endIndex);
        String symbol = lookupSymbol(str, beginIndex, endIndex, hash);
        if (symbol == null) {
            symbol = str.substring(beginIndex, endIndex);
            storeSymbol(symbol, hash);
        }
        return symbol;
    }

    public String getSymbol(char[] str, int offset, int length) {
        int hash = computeHash(str, offset, length);
        String symbol = lookupSymbol(str, offset, length, hash);
        if (symbol == null) {
            symbol = new String(str, offset, length);
            storeSymbol(symbol, hash);
        }
        return symbol;
    }
}
