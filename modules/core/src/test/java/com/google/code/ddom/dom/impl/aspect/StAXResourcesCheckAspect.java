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
package com.google.code.ddom.dom.impl.aspect;

import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLStreamReader;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Aspect that checks that resources acquired from StAX are correctly released during each unit
 * test.
 * 
 * @author Andreas Veithen
 */
@Aspect
public class StAXResourcesCheckAspect {
    private final Set<XMLStreamReader> openReaders = new HashSet<XMLStreamReader>();
    private int level;
    
    @Around("execution(void org.junit.internal.runners.MethodRoadie.run())" +
    		" || execution(void org.w3c.domts.JUnitTestCaseAdapter.runTest())")
    public void aroundTestCase(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        level++;
        thisJoinPoint.proceed();
        level--;
        if (level == 0 && !openReaders.isEmpty()) {
            System.out.println("Unclosed stream readers detected!");
            openReaders.clear();
        }
    }
    
    @AfterReturning(pointcut="call(javax.xml.stream.XMLStreamReader javax.xml.stream.XMLInputFactory.*(..))", returning="reader")
    public void afterStreamReaderCreated(XMLStreamReader reader) {
        if (level == 0) {
            System.out.println("Stream reader created outside of a test case?!?");
        } else {
            openReaders.add(reader);
        }
    }

    @After("call(void javax.xml.stream.XMLStreamReader.close()) && target(reader)")
    public void afterStreamReaderClosed(XMLStreamReader reader) {
        openReaders.remove(reader);
    }
}
