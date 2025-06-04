import java.util.Map;
import java.util.HashMap;
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
}
