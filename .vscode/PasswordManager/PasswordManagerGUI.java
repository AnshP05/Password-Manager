import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

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
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);
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
        System.out.println("Password Manager GUI initialized.");
    }

    // Main method to run the application
    public static void main(String[] args) {
        // Create an instance of the PasswordManagerGUI class to start the application
        PasswordManagerGUI passwordManager = new PasswordManagerGUI();
    }
}