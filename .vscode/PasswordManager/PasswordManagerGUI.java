import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.util.Map.Entry;
import java.awt.BorderLayout;
import javax.swing.JFileChooser;

public class PasswordManagerGUI {

    private PasswordStore passwordStore; // Instance of the PasswordStore class
    // Constructor for the PasswordManagerGUI class
    public PasswordManagerGUI() {
        // Initialize the GUI components and set up the application
        initialize();
    }

    // Method to initialize the GUI components
    private void initialize() {
    
        // Code to set up the GUI goes here
        JFrame frame = new JFrame("Password Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Password");
        JButton viewButton = new JButton("View Passwords");
        JButton deleteButton = new JButton("Delete Password");
        JButton exportButton = new JButton("Export Passwords");
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exportButton);
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.setVisible(true);

        // Create an instance of PasswordStore to manage passwords
        passwordStore = new PasswordStore();
        addButton.addActionListener(e -> {
            // Code to add a password entry
            String key = JOptionPane.showInputDialog(frame, "Enter key for password:");
            if (key == null || key.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Key cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String password = JOptionPane.showInputDialog(frame, "Enter password for " + key + ":");
            if (password == null || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            passwordStore.addEntry(key, password);
            System.out.println("Password added for key: " + key);
        });

        viewButton.addActionListener(e -> {
            // Code to view all passwords
            if(passwordStore.getAllEntries().isEmpty()){
                JOptionPane.showMessageDialog(frame, "No passwords stored yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            StringBuilder allPasswords = new StringBuilder("Stored Passwords:\n");
            for (Entry<String, String> entry : passwordStore.getAllEntries().entrySet()) {
                allPasswords.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            JOptionPane.showMessageDialog(frame, allPasswords.toString(), "View Passwords", JOptionPane.INFORMATION_MESSAGE);
        });

        deleteButton.addActionListener(e -> {
            // Code to delete a password entry
            String key = JOptionPane.showInputDialog(frame, "Enter key of password to delete:");
            if (key == null || key.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Key cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!passwordStore.getAllEntries().containsKey(key)) {
                JOptionPane.showMessageDialog(frame, "No password found for the given key.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            passwordStore.deleteEntry(key);
            System.out.println("Password deleted for key: " + key);
        });

        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int userSelection = fileChooser.showSaveDialog(frame);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                passwordStore.exportToFile(filePath);
                JOptionPane.showMessageDialog(frame, "Passwords exported successfully to " + filePath, "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Export cancelled.", "Export Cancelled", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    // Main method to run the application
    public static void main(String[] args) {
        // Create an instance of the PasswordManagerGUI class to start the application
        PasswordManagerGUI passwordManager = new PasswordManagerGUI();
    }
}