package fr.imt_atlantique.frappe.exceptions;

public class BadRequestException extends ApplicationException {
    public BadRequestException(String message) {
        super(message);
    }
}
