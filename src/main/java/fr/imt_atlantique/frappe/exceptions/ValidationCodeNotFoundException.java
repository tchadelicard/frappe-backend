package fr.imt_atlantique.frappe.exceptions;

public class ValidationCodeNotFoundException extends BadRequestException {
    public ValidationCodeNotFoundException(String message) {
        super(message);
    }

}
