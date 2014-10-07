package ch.tkayser.budget.exception;

import java.lang.reflect.Field;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

public class BudgetExceptionInterceptor {

    @AroundInvoke
    public Object wrapMethod(InvocationContext invContext) throws Exception {
        try {
            Object result = invContext.proceed();
            return result;
        } catch (Exception e) {
            throw wrapException(e);
        }

    }

    private Exception wrapException(Exception t) throws Exception {

        // init the result
        Exception result = t;

        // wrap all not Budget Exceptions
        Exception currentException = t;
        Exception wrappedException = null;
        Exception prevException = null;

        while (currentException != null) {

            // special treatment for root exceptions
            if (currentException == t) {
                
                // wrap the exception
                wrappedException = getWrappedException(currentException);    
                result = wrappedException;
                
                // special treatment for some exceptions
                if (currentException instanceof OptimisticLockException) {
                    // optimistic locking exception
                    result = new BudgetException("Concurrent update!");
                    prevException = result;
                } else if (currentException instanceof PersistenceException) {
                    // wrap general database exception
                    result = new BudgetException("Datenbank fehler");
                    prevException = result;
                }

            } else {
                // wrap the excepton
                wrappedException = getWrappedException(currentException);
            }

            // set parent if there is one
            if (prevException != null) {
                if (prevException.getCause() == null) {
                    prevException.initCause(wrappedException);                    
                } else {
                    // allready set. -> overwrite with reflection
                    Field causeField = Throwable.class.getDeclaredField("cause");
                    causeField.setAccessible(true);
                    causeField.set(prevException, wrappedException);
                }
            }

            // next exception
            prevException = wrappedException;
            currentException = (Exception) currentException.getCause();

        }

        // return the wrapped exception
        return result;

    }

    /**
     * return a wrapped intsance of the exception
     * @param e
     * @return
     */
    private Exception getWrappedException(Exception e) {
        // no wrapping for budget exceptions
        if (e instanceof BudgetException) {
            return e;
        } else {
            // wrap other exceptions
            return new WrappedBudgetException(e);
        }
    }

}
