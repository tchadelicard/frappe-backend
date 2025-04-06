package fr.imt_atlantique.frappe.exceptions;

public class UserAlreadyExistsException extends BadRequestException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
