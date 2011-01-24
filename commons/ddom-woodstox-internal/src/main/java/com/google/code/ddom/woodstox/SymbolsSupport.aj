/*
 * Copyright 2009 Andreas Veithen
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
package com.google.code.ddom.woodstox;

import com.ctc.wstx.util.SymbolTable;
import com.googlecode.ddom.symbols.Symbols;

/**
 * Aspect that adds the {@link Symbols} interface to the existing {@link SymbolTable} class.
 * 
 * @author Andreas Veithen
 */
public privileged aspect SymbolsSupport {
    declare parents: SymbolTable implements Symbols;

    // This mainly contains code copied from findSymbol(String)
    public String SymbolTable.lookupSymbol(String str) {
        int len = str.length();
        // Sanity check:
        if (len < 1) {
            return EMPTY_STRING;
        }

        int index = calcHash(str) & mIndexMask;
        String sym = mSymbols[index];

        // Optimal case; checking existing primary symbol for hash index:
        if (sym != null) {
            // Let's inline primary String equality checking:
            if (sym.length() == len) {
                int i = 0;
                for (; i < len; ++i) {
                    if (sym.charAt(i) != str.charAt(i)) {
                        break;
                    }
                }
                // Optimal case; primary match found
                if (i == len) {
                    return sym;
                }
            }
            // How about collision bucket?
            Bucket b = mBuckets[index >> 1];
            if (b != null) {
                sym = b.find(str);
                if (sym != null) {
                    return sym;
                }
            }
        }
        
        return null;
    }

    public String SymbolTable.getSymbol(String str) {
        return findSymbol(str);
    }
    
    public String SymbolTable.lookupSymbol(char[] str, int offset, int length) {
        return findSymbolIfExists(str, offset, length, calcHash(str, offset, length));
    }
    
    public String SymbolTable.getSymbol(char[] str, int offset, int length) {
        return findSymbol(str, offset, length, calcHash(str, offset, length));
    }

    // This mainly contains code copied from findSymbol(String)
    private void SymbolTable.storeSymbol(String str) {
        // Need to expand?
        if (mSize >= mSizeThreshold) {
            rehash();
        } else if (!mDirty) {
            // Or perhaps we need to do copy-on-write?
            copyArrays();
            mDirty = true;
        }
        ++mSize;
        
        int index = calcHash(str) & mIndexMask;

        if (mInternStrings) {
            str = str.intern();
        }
        // Ok; do we need to add primary entry, or a bucket?
        if (mSymbols[index] == null) {
            mSymbols[index] = str;
        } else {
            int bix = index >> 1;
            mBuckets[bix] = new Bucket(str, mBuckets[bix]);
        }
    }
    
    // See other calcHash methods
    public static int SymbolTable.calcHash(String str, int beginIndex, int endIndex) {
        int hash = str.charAt(beginIndex);
        for (int i = beginIndex+1; i < endIndex; ++i) {
            hash = (hash * 31) + str.charAt(i);
        }
        return hash;
    }
    
    // See find(char[], int, int)
    public String SymbolTable.Bucket.find(String str, int beginIndex, int endIndex) {
        int len = endIndex-beginIndex;
        String sym = mSymbol;
        Bucket b = mNext;

        while (true) { // Inlined equality comparison:
            if (sym.length() == len) {
                int i = 0;
                do {
                    if (sym.charAt(i) != str.charAt(beginIndex+i)) {
                        break;
                    }
                } while (++i < len);
                if (i == len) {
                    return sym;
                }
            }
            if (b == null) {
                break;
            }
            sym = b.getSymbol();
            b = b.getNext();
        }
        return null;
    }
    
    // See findSymbolIfExists(char[], int, int, int)
    public String SymbolTable.lookupSymbol(String str, int beginIndex, int endIndex) {
        int len = endIndex-beginIndex;
        // Sanity check:
        if (len < 1) {
            return EMPTY_STRING;
        }
        int index = calcHash(str, beginIndex, endIndex) & mIndexMask;

        String sym = mSymbols[index];
        // Optimal case; checking existing primary symbol for hash index:
        if (sym != null) {
            // Let's inline primary String equality checking:
            if (sym.length() == len) {
                int i = 0;
                do {
                    if (sym.charAt(i) != str.charAt(beginIndex+i)) {
                        break;
                    }
                } while (++i < len);
                // Optimal case; primary match found
                if (i == len) {
                    return sym;
                }
            }
            // How about collision bucket?
            Bucket b = mBuckets[index >> 1];
            if (b != null) {
                sym = b.find(str, beginIndex, endIndex);
                if (sym != null) {
                    return sym;
                }
            }
        }
        return null;
    }

    public String SymbolTable.getSymbol(String str, int beginIndex, int endIndex) {
        String sym = lookupSymbol(str, beginIndex, endIndex);
        if (sym == null) {
            sym = str.substring(beginIndex, endIndex);
            storeSymbol(sym);
        }
        return sym;
    }
}
