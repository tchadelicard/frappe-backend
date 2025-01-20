package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.EncryptionResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionService {

    @Value("${frappe.security.encryption.secret-key}")
    private String secretKey;

    private static final int SALT_LENGTH = 16; // 16 bytes
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256; // 256 bits
    private static final int GCM_TAG_LENGTH = 16; // 16 bytes (128 bits)

    // Generate a random salt
    public byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    // Derive a SecretKey using the configured secretKey and a salt
    private SecretKey deriveKey(byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    // Encrypt the data
    public String encrypt(String data, byte[] salt, byte[] iv) throws Exception {
        SecretKey secretKey = deriveKey(salt);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt the data
    public String decrypt(String encryptedData, byte[] salt, byte[] iv) throws Exception {
        SecretKey secretKey = deriveKey(salt);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    // Public method to encrypt data and prepare salt/IV
    public EncryptionResult encryptAndPrepareData(String data) throws Exception {
        byte[] salt = generateSalt(); // Generate a new salt
        byte[] iv = new byte[12]; // 12 bytes for GCM
        new SecureRandom().nextBytes(iv);

        String encryptedData = encrypt(data, salt, iv);

        return EncryptionResult.builder()
                .encryptedData(encryptedData)
                .salt(Base64.getEncoder().encodeToString(salt))
                .iv(Base64.getEncoder().encodeToString(iv))
                .build();
    }

    // Public method to decrypt the data using salt and IV
    public String decryptData(String encryptedData, String saltBase64, String ivBase64) throws Exception {
        byte[] salt = Base64.getDecoder().decode(saltBase64);
        byte[] iv = Base64.getDecoder().decode(ivBase64);
        return decrypt(encryptedData, salt, iv);
    }
}