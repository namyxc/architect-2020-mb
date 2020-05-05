package bank;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class IllegalCreditException extends Exception {
    public IllegalCreditException(String message) {
        super(message);
    }
}
