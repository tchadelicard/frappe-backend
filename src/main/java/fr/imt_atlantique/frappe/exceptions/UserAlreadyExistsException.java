package fr.imt_atlantique.frappe.exceptions;

public class UserAlreadyExistsException extends ApplicationException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
