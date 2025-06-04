import java.util.Map;

import javax.swing.JOptionPane;

import java.util.HashMap;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;

public class PasswordStore {
    //intance variable
    private Map<String, String> passwordMap;

    public PasswordStore() {
        // Initialize the password map
        passwordMap = new HashMap<>();
    }

    public void addEntry(String key, String password) {
        // Add a new entry to the password store
        passwordMap.put(key, password);
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

    public void exportToFile(String filePath) {
        // Code to export the password map to a file
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
}
