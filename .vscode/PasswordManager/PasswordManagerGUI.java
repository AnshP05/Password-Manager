import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.FlowLayout;
import java.util.Map.Entry;
import java.awt.BorderLayout;
import javax.swing.JFileChooser;

public class PasswordManagerGUI {

    // Instance variable to hold the PasswordStore object
    private PasswordStore passwordStore; 

    // Constructor for the PasswordManagerGUI class
    public PasswordManagerGUI() {
        // Initialize the GUI components and set up the application
        initialize();
    }

    // Method to initialize the GUI components
    private void initialize() {
    
        /*
        * Creating the main frame for the password manager application
        * Sets the default close operation to exiting when the close button is pressed
        * Sets the size of the frame to a normal size for the user to use without too much clutter
        * Sets the frame's layout to BorderLayout which has 5 regions(North, South, West, East, Center)
        */ 
        JFrame frame = new JFrame("Password Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 200);
        frame.setLayout(new BorderLayout());

        /* 
        * Creating a panel for buttons at the top of the frame
        * Setting the layout of the panel to FlowLayout(arranges the components in a row much like text in a paragraph) with left alignment
        * Creating and adding buttons to the button panel for various actions like adding, viewing, deleting, and exporting/importing passwords
        * Adding the button panel to the frame at the top (North) region
        * Setting the frame to be visible so that the user can interact with it
        */

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Password");
        JButton viewButton = new JButton("View Passwords");
        JButton deleteButton = new JButton("Delete Password");
        JButton exportButton = new JButton("Export Passwords");
        JButton importButton = new JButton("Import Passwords");
        JButton editPasswordsButton = new JButton("Edit Existing Passwords");
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(importButton);
        buttonPanel.add(editPasswordsButton);
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.setVisible(true);

        // Create an instance of PasswordStore to manage passwords
        passwordStore = new PasswordStore();

        /*
        * Adds an action listener to the "Add Password" button.
        * 
        * When clicked:
        * - Prompts the user to enter a key (e.g., a website or service name) using JOptionPane.
        *   - If the key is null (user canceled) or empty (user left it blank), it shows an error dialog and exits the listener early.
        * 
        * - Then prompts the user to enter a password for the given key.
        *   - Again, it checks if the password is null or empty and shows an error dialog if so.
        * 
        * - If both values are valid:
        *   - It calls the addEntry() method of the PasswordStore to store the key-password pair.
        * 
        * - Finally, it shows a confirmation message indicating that the password was added successfully.
        * 
        * Note:
        * - This step ensures basic input validation.
        * - JOptionPane is used for a simple and interactive way to get user input and display feedback.
        */

        addButton.addActionListener(e -> {
            // Code to add a password entry

            String key = JOptionPane.showInputDialog(frame, "Enter key for password:");
            if (key == null || key.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Key cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String password;
            while(true) {
                JPasswordField passwordField = new JPasswordField(20);
                JPasswordField passwordField2 = new JPasswordField(20);

                JPanel passwordPanel = new JPanel(new java.awt.GridLayout(2, 2));
                passwordPanel.add(new javax.swing.JLabel("Enter password:"));
                passwordPanel.add(passwordField);
                passwordPanel.add(new javax.swing.JLabel("Re-enter password:"));
                passwordPanel.add(passwordField2);
                int result = JOptionPane.showConfirmDialog(frame, passwordPanel, "Enter password for key: " + key, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result != JOptionPane.OK_OPTION) {
                    return;
                }
                password = new String(passwordField.getPassword());
                String password2 = new String(passwordField2.getPassword());
                if(password.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                if(!password.equals(password2)) {
                    JOptionPane.showMessageDialog(frame, "Passwords do not match. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                } else if(!passwordStore.isPasswordStrong(password)){
                    JOptionPane.showMessageDialog(frame, "Password is not strong enough. It must be at least 8 characters long, contain uppercase and lowercase letters, numbers, and special characters.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    break;
                }
            } 
            passwordStore.addEntry(key, password);
            JOptionPane.showMessageDialog(frame, "Password added for key: " + key, "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        /*
        * Adds an action listener to the "View Passwords" button.
        *
        * When clicked:
        * - It checks if any passwords exist in the store. If not, it notifies the user and exits.
        * - Otherwise, it opens a custom panel with:
        *   - A search bar (JTextField) at the top for filtering passwords by their key (e.g., website name).
        *   - A toggle button (JToggleButton) to show or hide decrypted passwords.
        *   - A scrollable text area (JTextArea inside JScrollPane) displaying the filtered password list.
        *
        * Features:
        * - The password list updates dynamically as the user types into the search bar.
        *   - This is achieved using a DocumentListener attached to the JTextField.
        * - The toggle button allows switching between masked ("********") and decrypted password views.
        *
        * Technical Notes:
        * - The updatePasswords Runnable handles the filtering and view logic.
        * - Entries are filtered using a case-insensitive match of the key against the search field.
        * - The decrypted password is only shown if the toggle button is selected.
        * - Uses layout managers (BorderLayout) for neat organization of UI elements.
        */

        viewButton.addActionListener(e -> {
            // Code to view all passwords
            if(passwordStore.getAllEntries().isEmpty()){
                JOptionPane.showMessageDialog(frame, "No passwords stored yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JTextField searchField = new JTextField(20);
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(new javax.swing.JLabel("Search: "), BorderLayout.WEST);
            topPanel.add(searchField, BorderLayout.CENTER);
            JTextArea passwordArea = new JTextArea(15, 30);
            passwordArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(passwordArea);

            JToggleButton toggleButton = new JToggleButton("Show Passwords");

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);
            panel.add(toggleButton, BorderLayout.SOUTH);

            Runnable updatePasswords = () -> {
                StringBuilder filteredPasswords = new StringBuilder("Stored Passwords:\n");
                String searchText = searchField.getText().trim().toLowerCase();
                for (Entry<String, String> entry : passwordStore.getAllEntries().entrySet()) {
                    String key = entry.getKey().toLowerCase();
                    if(key.contains(searchText)){
                        String password = toggleButton.isSelected() ? passwordStore.decrypt(entry.getValue()) : "********";
                        filteredPasswords.append(entry.getKey()).append(": ").append(password).append("\n");
                    }
                }
                passwordArea.setText(filteredPasswords.toString());
            };

            searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    updatePasswords.run();
                }

                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    updatePasswords.run();
                }

                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    updatePasswords.run();
                }
            });

            updatePasswords.run();

            toggleButton.addActionListener(e2 -> {
                toggleButton.setText(toggleButton.isSelected() ? "Hide Passwords" : "Show Passwords");
                updatePasswords.run();
            });

            JOptionPane.showMessageDialog(frame, panel, "View Passwords", JOptionPane.INFORMATION_MESSAGE);
        });

        /*
         * Adds an action listener to the "Export Passwords" button.
         * 
         * When clicked:
         * - It creates a JFileChooser to allow the user to select a file path for exporting passwords.
         * - If the user approves the selection (i.e., clicks "Save"):
         *   - It retrieves the selected file path using getSelectedFile().getAbsolutePath().
         *   - It calls the exportToFile() method of PasswordStore to save all passwords to the specified file.
         *   - Finally, it shows a success message indicating that passwords were exported successfully.
         * 
         * - If the user cancels the operation, it shows a message indicating that export was cancelled.
         */
        exportButton.addActionListener(e -> {

            Object[] options = {"Plain Text", "Encrypted"};
            int choice = JOptionPane.showOptionDialog(frame, "Choose export format: ", "Export Format", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if(choice == JOptionPane.CLOSED_OPTION) {
                JOptionPane.showMessageDialog(frame, "Export cancelled.", "Export Cancelled", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            int userSelection = fileChooser.showSaveDialog(frame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if(choice == 0) {
                    passwordStore.exportToFilePlain(filePath);
                } else {
                    passwordStore.exportToFileEncrypted(filePath);
                }

                JOptionPane.showMessageDialog(frame, "Passwords exported successfully to " + filePath, "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Export cancelled.", "Export Cancelled", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        /*
         * Adds an action listener to the "Import Passwords" button.
         * 
         * When clicked:
         * - It creates a JFileChooser to allow the user to select a file path for importing passwords.
         * - If the user approves the selection (i.e., clicks "Open"):
         *   - It retrieves the selected file path using getSelectedFile().getAbsolutePath().
         *   - It calls the importFromFile() method of PasswordStore to load passwords from the specified file.
         *   - Finally, it shows a success message indicating that passwords were imported successfully.
         * 
         * - If the user cancels the operation, it shows a message indicating that import was cancelled.
         */
        importButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int userSelection = fileChooser.showOpenDialog(frame);
            if (userSelection == fileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                passwordStore.importFromFile(filePath);
                JOptionPane.showMessageDialog(frame, "Passwords imported successfully from " + filePath, "Import Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Import cancelled.", "Import Cancelled", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        editPasswordsButton.addActionListener(e -> {

            JTextField searchField = new JTextField(20);
            DefaultListModel<String> listModel = new DefaultListModel<>();
            JList<String> entryList = new JList<>(listModel);
            JScrollPane scrollPane = new JScrollPane(entryList);
            JButton editButton = new JButton("Edit Selected Password");

            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(new javax.swing.JLabel("Search: "), BorderLayout.WEST);
            topPanel.add(searchField, BorderLayout.CENTER);

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(topPanel, BorderLayout.NORTH);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
            mainPanel.add(editButton, BorderLayout.SOUTH);

            passwordStore.getAllEntries().keySet().forEach(listModel::addElement);   

            searchField.getDocument().addDocumentListener(new DocumentListener() {
                private void updateList(){
                    String searchText = searchField.getText().trim().toLowerCase();
                    listModel.clear();
                    for (String key : passwordStore.getAllEntries().keySet()) {
                        if (key.toLowerCase().contains(searchText)) {
                            listModel.addElement(key);
                        }
                    }
                }

                public void insertUpdate(DocumentEvent e) {
                    updateList();
                }
                public void removeUpdate(DocumentEvent e) {
                    updateList();
                }
                public void changedUpdate(DocumentEvent e) {
                    updateList();
                }
            });

            if(passwordStore.getAllEntries().isEmpty()){
                JOptionPane.showMessageDialog(frame, "No passwords stored yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int dialogResult = JOptionPane.showConfirmDialog(frame, mainPanel, "Edit Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if(dialogResult == JOptionPane.OK_OPTION && entryList.getSelectedValue() != null) {
                String key = entryList.getSelectedValue();
                String newPassword;

                 while(true) {
                JPasswordField passwordField = new JPasswordField(20);
                JPasswordField passwordField2 = new JPasswordField(20);

                JPanel passwordPanel = new JPanel(new java.awt.GridLayout(2, 2));
                passwordPanel.add(new javax.swing.JLabel("Enter password:"));
                passwordPanel.add(passwordField);
                passwordPanel.add(new javax.swing.JLabel("Re-enter password:"));
                passwordPanel.add(passwordField2);
                int result = JOptionPane.showConfirmDialog(frame, passwordPanel, "Enter password for key: " + key, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result != JOptionPane.OK_OPTION) {
                    return;
                }
                newPassword = new String(passwordField.getPassword());
                String password2 = new String(passwordField2.getPassword());
                if(newPassword.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                if(!newPassword.equals(password2)) {
                    JOptionPane.showMessageDialog(frame, "Passwords do not match. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                } else if(!passwordStore.isPasswordStrong(newPassword)){
                    JOptionPane.showMessageDialog(frame, "Password is not strong enough. It must be at least 8 characters long, contain uppercase and lowercase letters, numbers, and special characters.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    break;
                }
            } 
            passwordStore.addEntry(key, newPassword);
            JOptionPane.showMessageDialog(frame, "Password updated for key: " + key, "Info", JOptionPane.INFORMATION_MESSAGE);

            }
        });
    }

    // Main method to run the application
    public static void main(String[] args) {
        // Create an instance of the PasswordManagerGUI class to start the application
        PasswordManagerGUI passwordManager = new PasswordManagerGUI();
    }
}