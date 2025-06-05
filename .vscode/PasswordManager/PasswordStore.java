// Import necessary Java classes for data structures, file I/O, encryption, and GUI dialogs
import java.util.Map;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import javax.swing.JOptionPane;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class PasswordStore {
    // Map to store password entries with keys (e.g., website names) and their corresponding encrypted passwords
    private Map<String, String> passwordMap;

    // Constructor initializes the password map
    public PasswordStore() {
        passwordMap = new HashMap<>();
    }

    /**
     * Adds a new password entry to the store.
     * The password is encrypted before being stored.
     *
     * @param key      The identifier for the password (e.g., "Gmail").
     * @param password The plaintext password to be stored.
     */
    public void addEntry(String key, String password) {
        String encryptedPassword = encrypt(password);
        passwordMap.put(key, encryptedPassword);
    }

    /**
     * Checks if a given password is strong based on defined criteria:
     * - Minimum length of 8 characters
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one digit
     * - At least one special character from the set !@#$%^&*()
     *
     * @param password The password to check.
     * @return true if the password meets all criteria; false otherwise.
     */
    public boolean isPasswordStrong(String password) {
        if (password.length() < 8) return false;
        if (!password.matches(".*[A-Z].*")) return false; // At least one uppercase letter
        if (!password.matches(".*[a-z].*")) return false; // At least one lowercase letter
        if (!password.matches(".*\\d.*")) return false;   // At least one digit
        if (!password.matches(".*[!@#$%^&*()].*")) return false; // At least one special character
        return true;
    }

    /**
     * Retrieves all password entries.
     *
     * @return A map containing all key-encryptedPassword pairs.
     */
    public Map<String, String> getAllEntries() {
        return passwordMap;
    }

    /**
     * Deletes a password entry associated with the given key.
     *
     * @param key The key whose entry is to be deleted.
     */
    public void deleteEntry(String key) {
        if (passwordMap.containsKey(key)) {
            passwordMap.remove(key);
        }
    }

    /**
     * Exports all password entries to a file in plaintext format.
     * Each line in the file will have the format: key:password
     *
     * @param filePath The path to the file where passwords will be exported.
     */
    public void exportToFilePlain(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, String> entry : passwordMap.entrySet()) {
                String line = entry.getKey() + ":" + decrypt(entry.getValue());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error exporting passwords: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exports all password entries to a file in encrypted format.
     * Each line in the file will have the format: key:encryptedPassword
     *
     * @param filePath The path to the file where passwords will be exported.
     */
    public void exportToFileEncrypted(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, String> entry : passwordMap.entrySet()) {
                String line = entry.getKey() + ":" + entry.getValue();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error exporting passwords: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Imports password entries from a file.
     * Each line in the file should have the format: key:password
     * The passwords are encrypted before being stored.
     *
     * @param filePath The path to the file from which passwords will be imported.
     */
    public void importFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String password = parts[1].trim();
                        addEntry(key, password);
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error importing passwords from file: " + e.getMessage(), "Import Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Encrypts a plaintext password using AES encryption.
     * The encrypted password is returned as a Base64-encoded string.
     *
     * @param plainText The plaintext password to encrypt.
     * @return The encrypted password as a Base64-encoded string.
     */
    public String encrypt(String plainText) {
        try {
            String key = "1234567890123456"; // 16-character secret key for AES
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error encrypting password: " + e.getMessage(), "Encryption Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Decrypts a Base64-encoded, AES-encrypted password back to plaintext.
     *
     * @param encryptedText The encrypted password as a Base64-encoded string.
     * @return The decrypted plaintext password.
     */
    public String decrypt(String encryptedText) {
        try {
            String key = "1234567890123456"; // Same 16-character secret key used for encryption
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error decrypting password: " + e.getMessage(), "Decryption Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
