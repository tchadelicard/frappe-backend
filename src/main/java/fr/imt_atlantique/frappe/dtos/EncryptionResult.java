package fr.imt_atlantique.frappe.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EncryptionResult {
    private String encryptedData;
    private String salt;
    private String iv;
}
