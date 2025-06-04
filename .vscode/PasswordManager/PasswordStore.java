import java.util.Map;

import javax.swing.JOptionPane;

import java.util.HashMap;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
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
}
