package fr.imt_atlantique.frappe.exceptions;

public class FailedToSendEmailException extends ApplicationException {
    public FailedToSendEmailException(String message) {
        super(message);
    }
}
