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
package com.google.code.ddom.utils.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

public class ValidatedTestRunner extends JUnit4ClassRunner {
    private static class MultiRunListener extends RunListener {
        private final RunNotifier notifier;
        private boolean firstRun = true;
        private int runs;
        private String failureMessage;
        
        public MultiRunListener(RunNotifier notifier, int runs) {
            this.notifier = notifier;
            this.runs = runs;
        }

        @Override
        public void testStarted(Description description) throws Exception {
            runs--;
            if (firstRun) {
                notifier.fireTestStarted(description);
                firstRun = false;
            }
        }
        
        @Override
        public void testFailure(Failure failure) throws Exception {
            if (failureMessage != null) {
                failure = new Failure(failure.getDescription(), new Error(failureMessage,
                        failure.getException()));
            }
            notifier.fireTestFailure(failure);
            runs = 0;
        }

        @Override
        public void testIgnored(Description description) throws Exception {
            notifier.fireTestIgnored(description);
            runs = 0;
        }

        @Override
        public void testFinished(Description description) throws Exception {
            if (runs == 0) {
                notifier.fireTestFinished(description);
            }
        }
        
        public void setFailureMessage(String failureMessage) {
            this.failureMessage = failureMessage;
        }

        public boolean isShouldContinue() {
            return runs > 0;
        }
    }
    
    private boolean isReferenceEnvironment;
    
    public ValidatedTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected void invokeTestMethod(Method method, RunNotifier notifier) {
        boolean validate = method.getAnnotation(Validated.class) != null;
        RunNotifier multiRunNotifier = new RunNotifier();
        MultiRunListener multiRunListener = new MultiRunListener(notifier, validate ? 2 : 1);
        multiRunNotifier.addListener(multiRunListener);
        if (validate) {
            isReferenceEnvironment = true;
            multiRunListener.setFailureMessage(
                    "Invalid test case; execution failed in validation environment");
            super.invokeTestMethod(method, multiRunNotifier);
        }
        if (multiRunListener.isShouldContinue()) {
            isReferenceEnvironment = false;
            multiRunListener.setFailureMessage(null);
            super.invokeTestMethod(method, multiRunNotifier);
        }
    }

    private <T> T createInstance(Class<T> clazz) throws Exception {
        Field field;
        try {
            // TODO: this should be documented in the JavaDoc of ValidatedTestResource
            field = clazz.getDeclaredField("INSTANCE");
        } catch (NoSuchFieldException ex) {
            field = null;
        }
        if (field != null) {
            return clazz.cast(field.get(null));
        } else {
            return clazz.newInstance();
        }
    }
    
    @Override
    protected Object createTest() throws Exception {
        Object test = super.createTest();
        for (Field field : test.getClass().getDeclaredFields()) {
            ValidatedTestResource annotation = field.getAnnotation(ValidatedTestResource.class);
            if (annotation != null) {
                Class<?> clazz = isReferenceEnvironment ? annotation.reference() : annotation.actual();
                field.setAccessible(true);
                field.set(test, createInstance(clazz));
            }
        }
        return test;
    }
}
