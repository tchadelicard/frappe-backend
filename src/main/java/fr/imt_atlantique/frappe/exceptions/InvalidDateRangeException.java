package fr.imt_atlantique.frappe.exceptions;

public class InvalidDateRangeException extends BadRequestException {
    public InvalidDateRangeException(String message) {
        super(message);
    }

}
