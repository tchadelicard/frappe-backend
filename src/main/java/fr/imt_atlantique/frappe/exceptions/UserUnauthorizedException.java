package fr.imt_atlantique.frappe.exceptions;

public class UserUnauthorizedException extends ApplicationException {
    public UserUnauthorizedException(String message) {
        super(message);
    }

}
