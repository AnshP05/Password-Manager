import java.util.Map;

import javax.swing.JOptionPane;

import java.util.HashMap;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class PasswordStore {
    //intance variable
    private Map<String, String> passwordMap;

    public PasswordStore() {
        // Initialize the password map
        passwordMap = new HashMap<>();
    }

    public void addEntry(String key, String password) {
        if(password != null && !password.isEmpty()) {
            String encryptedPassword = encrypt(password);
            passwordMap.put(key, encryptedPassword); 
        } 
    }

    public Map<String, String> getAllEntries() {
        // Return all entries in the password store
        return passwordMap;
    }

    public void deleteEntry(String key){
        if(passwordMap.containsKey(key)) {
            // Remove the entry with the specified key
            passwordMap.remove(key);
        } 
    }

    public void exportToFilePlain(String filePath) {
        // Code to export the password map to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for(Map.Entry<String, String> entry : passwordMap.entrySet()) {
                String line = entry.getKey() + ":" + decrypt(entry.getValue());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error exporting passwords: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void exportToFileEncrypted(String filePath) {
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for(Map.Entry<String, String> entry : passwordMap.entrySet()) {
                String line = entry.getKey() + ":" + entry.getValue();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error exporting passwords: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
            
    }

    public void importFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line = reader.readLine()) != null) {
                if(!line.trim().isEmpty()) {
                    String[] parts = line.split(":");
                    if(parts.length == 2) {
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
    * Encrypts a plaintext string (e.g., a password) using AES encryption and returns the result as a Base64-encoded string.
    *
    * How it works:
    * - A static 16-character secret key is defined, which is required for 128-bit AES encryption.
    * - The key is converted into a SecretKeySpec, a format the Cipher class understands.
    * - A Cipher instance is initialized in ENCRYPT_MODE with the AES algorithm.
    * - The plaintext string is converted into bytes and then encrypted using the Cipher's doFinal method.
    * - The resulting encrypted byte array is then encoded to a Base64 string so it can be safely stored as text.
    *
    * If an error occurs during encryption, a dialog box shows the error message and the method returns null.
    */

    public String encrypt(String plainText) {
        try {
            String key = "1234567890123456"; // 16 char secret key
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
    * Decrypts a Base64-encoded, AES-encrypted string back into its original plaintext form.
    *
    * How it works:
    * - Uses the same static 16-character secret key that was used for encryption.
    * - Converts this key into a SecretKeySpec to be used with the Cipher.
    * - A Cipher instance is initialized in DECRYPT_MODE with AES algorithm.
    * - The encrypted Base64 string is first decoded back into a byte array.
    * - The Cipher's doFinal method is used to decrypt the byte array into the original plaintext bytes.
    * - The decrypted byte array is then converted back into a regular String.
    *
    * If any error occurs during decryption, a dialog box shows the error message and the method returns null.
    */

    public String decrypt(String encryptedText) {
        try {
            String key = "1234567890123456";
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
