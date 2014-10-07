package ch.tkayser.budget.exception;

public class WrappedBudgetException extends BudgetException {

    private static final long serialVersionUID = -5446702389348431059L;

    /**
     * wrap an exception
     * 
     * @param otherException
     */
    public WrappedBudgetException(Throwable otherException) {
        // create a new
        // init exception just with the name and message of another one
        super(otherException.getClass().getName() + ": " + otherException.getMessage());

        // copy the stacktrace
        setStackTrace(otherException.getStackTrace());
    }

}
