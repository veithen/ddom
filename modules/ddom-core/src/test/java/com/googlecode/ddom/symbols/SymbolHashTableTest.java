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

import org.junit.Assert;
import org.junit.Test;

import com.googlecode.ddom.symbols.SymbolHashTable;
import com.googlecode.ddom.symbols.Symbols;

public class SymbolHashTableTest {
    @Test
    public void testLookupString() {
        Symbols symbols = new SymbolHashTable();
        String symbol = symbols.getSymbol("test");
        Assert.assertSame(symbol, symbols.lookupSymbol(new String(symbol)));
    }
    
    @Test
    public void testLookupSubstring() {
        Symbols symbols = new SymbolHashTable();
        String symbol = symbols.getSymbol("test");
        Assert.assertSame(symbol, symbols.lookupSymbol("xtestx", 1, 5));
    }
    
    @Test
    public void testLookupCharArray() {
        Symbols symbols = new SymbolHashTable();
        String symbol = symbols.getSymbol("test");
        Assert.assertSame(symbol, symbols.lookupSymbol("xtestx".toCharArray(), 1, 4));
    }
    
    @Test
    public void testCollisions() {
        Symbols symbols = new SymbolHashTable(2, 4f);
        String a = symbols.getSymbol("a");
        String b = symbols.getSymbol("b");
        String c = symbols.getSymbol("c");
        Assert.assertSame(a, symbols.getSymbol(new String("a")));
        Assert.assertSame(b, symbols.getSymbol(new String("b")));
        Assert.assertSame(c, symbols.getSymbol(new String("c")));
    }
    
    @Test
    public void testRehashing() {
        Symbols symbols = new SymbolHashTable(4, .75f);
        String[] s = new String[1000];
        for (int i=0; i<s.length; i++) {
            s[i] = symbols.getSymbol(String.valueOf(i));
        }
        for (int i=0; i<s.length; i++) {
            Assert.assertSame(s[i], symbols.getSymbol(String.valueOf(i)));
        }
    }
}
