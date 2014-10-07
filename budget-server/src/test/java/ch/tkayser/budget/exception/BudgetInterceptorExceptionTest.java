/*
 * Software is written by:
 *
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2009
 * 
 */
package ch.tkayser.budget.exception;

import java.lang.reflect.Method;
import java.util.Map;

import javax.interceptor.InvocationContext;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.junit.Assert;
import org.junit.Test;

import ch.tkayser.budget.test.BudgetBaseTest;

/**
 * @author tom
 * 
 */
public class BudgetInterceptorExceptionTest extends BudgetBaseTest {

    @Test
    public void testWrapException() {
        BudgetExceptionInterceptor interceptor = new BudgetExceptionInterceptor();

        // test special root exceptions
        testWithException(interceptor, new OptimisticLockException());
        testWithException(interceptor, new PersistenceException());

        // runtime root excetpion
        testWithException(interceptor, new RuntimeException("blabla"));

        // some nested ones (all to wrap)
        Exception e1 = new Exception("Cause");
        Exception e2 = new Exception("Super", e1);
        Exception rootException = new Exception("Root", e2);
        testWithException(interceptor, rootException);

        // root budget, rest to wrap
        rootException = new BudgetException("Root", e2);
        testWithException(interceptor, rootException);

        // mixed
        e1 = new BudgetException("Cause");
        e2 = new Exception("Super", e1);
        rootException = new BudgetException("Root", e2);
        testWithException(interceptor, rootException);

    }

    /**
     * @param interceptor
     * @param runtimeException
     */
    private void testWithException(BudgetExceptionInterceptor interceptor, Exception e) {
        try {
            interceptor.wrapMethod(new TestInvocationContext(e));
        } catch (Exception e1) {
            m_log.debug("WrappedException", e1);
            // check all exception up to the root one
            Throwable currentException = e1;
            while (currentException != null) {
                Assert.assertTrue("Exception not wrapped: " + currentException.getClass().getName(),
                        currentException instanceof BudgetException);
                currentException = currentException.getCause();
            }
        }
    }

    /**
     * Invocationcontext for tests
     * 
     * @author isc-kat
     * 
     */
    private class TestInvocationContext implements InvocationContext {

        private final Exception m_t;

        /**
         * create an invocation context for testing
         * 
         * @param t
         *            the exception that should be thrown when calling proceed
         */
        public TestInvocationContext(Exception t) {
            m_t = t;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.interceptor.InvocationContext#getContextData()
         */
        public Map<String, Object> getContextData() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.interceptor.InvocationContext#getMethod()
         */
        public Method getMethod() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.interceptor.InvocationContext#getParameters()
         */
        public Object[] getParameters() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.interceptor.InvocationContext#getTarget()
         */
        public Object getTarget() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.interceptor.InvocationContext#proceed()
         */
        public Object proceed() throws Exception {
            throw m_t;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.interceptor.InvocationContext#setParameters(java.lang.Object[])
         */
        public void setParameters(Object[] arg0) {
        }


    }
}
