import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
        * Adds an ActionListener to the "Add Password" button.
        * 
        * This block of code defines the behavior that happens when the user clicks on the "Add Password" button in the GUI.
        * It guides the user through:
        * - entering a key,
        * - confirming a password,
        * - validating strength,
        * - and finally storing the password securely.
        */
        addButton.addActionListener(e -> {
            /*
            * Prompt the user to enter a key (identifier for the password entry).
            * We use a while(true) loop to allow repeated attempts until:
            * - The key is non-empty,
            * - The user doesn't cancel,
            * - The key doesn't already exist.
            */
            String key;
            while (true) {
                key = JOptionPane.showInputDialog(frame, "Enter key for password:");

                // If user cancels or clicks "X", input will be null. In that case, exit the handler.
                if (key == null) return;

                // If the input is just whitespace or empty, show an error and prompt again.
                else if (key.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Key cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Check for duplicate keys to avoid overwriting an existing password.
                else if (passwordStore.getAllEntries().containsKey(key)) {
                    JOptionPane.showMessageDialog(frame, "Key already exists. Please enter a different key.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                // If all checks pass, break the loop and continue.
                else {
                    break;
                }
            }
            

            String password;  // This will hold the final confirmed password.

            /*
            * We use a while(true) loop to repeatedly ask the user to enter and confirm their password
            * until they meet all the conditions (non-empty, matching, strong).
            */
            while (true) {
                // Create two password fields for the user to input and confirm their password.
                JPasswordField passwordField = new JPasswordField(20);
                JPasswordField passwordField2 = new JPasswordField(20);

                /*
                * Create a JPanel with a GridLayout (3 rows, 2 columns) to neatly display both input prompts.
                * Using JLabel + JPasswordField in pairs makes it look like a proper form.
                */
                JPanel passwordPanel = new JPanel(new java.awt.GridLayout(3, 2));
                passwordPanel.add(new javax.swing.JLabel("Enter password:"));
                passwordPanel.add(passwordField);
                passwordPanel.add(new javax.swing.JLabel("Confirm password:"));
                passwordPanel.add(passwordField2);
                passwordPanel.add(new javax.swing.JLabel("Password must be at least 8 characters long, contain uppercase"));
                passwordPanel.add(new javax.swing.JLabel(" and lowercase letters, numbers, and special characters."));

                /*
                * Show a dialog with our custom panel and ask the user to confirm their input.
                * This dialog has OK and Cancel buttons.
                */
                int result = JOptionPane.showConfirmDialog(
                frame, passwordPanel,
                "Enter password for key: " + key,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
                );

                // If the user cancels (clicks Cancel or X), exit the loop and action.
                if (result != JOptionPane.OK_OPTION) {
                    return;
                }

                // Convert char[] from password field to String.
                password = new String(passwordField.getPassword());
                String password2 = new String(passwordField2.getPassword());

                // Check if the password is empty.
                if (password.trim().isEmpty()) {
                 JOptionPane.showMessageDialog(frame, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Ensure that the two entered passwords match.
                if (!password.equals(password2)) {
                 JOptionPane.showMessageDialog(frame, "Passwords do not match. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;  // Ask again
                }

                // Check for strong password requirements using a custom method.
                if (!passwordStore.isPasswordStrong(password)) {
                    JOptionPane.showMessageDialog(frame,
                    "Password is not strong enough. It must be at least 8 characters long, contain uppercase and lowercase letters, numbers, and special characters.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Password is valid and strong — exit loop.
                    break;
                }
            } // End of while loop for password confirmation

            /*
            * Finally, if the key and password pass all checks, add it to the password store.
            * This method also encrypts the password before saving it internally.
            */
            passwordStore.addEntry(key, password);

            // Confirm to the user that the password has been stored.
            JOptionPane.showMessageDialog(frame, "Password added for key: " + key, "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        /*
        * Adds an ActionListener to the "Delete Password" button.
        * 
        * This block defines what happens when a user wants to delete a saved password entry.
        * It guides the user through:
        * - checking if there are any passwords to delete,
        * - entering the key associated with the password they want to remove,
        * - validating that the key exists,
        * - and confirming the deletion.
        */
        deleteButton.addActionListener(e -> {
            /*
            * First, check if the password store is empty.
            * If there are no passwords saved, we inform the user and exit early.
            */

            if(passwordStore.getAllEntries().isEmpty()){
                JOptionPane.showMessageDialog(frame, "No passwords stored yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            /*
            * Prompt the user to enter the key of the password they want to delete.
            * We use JOptionPane for a simple dialog input.
            * - If the user cancels or closes the dialog (null), or inputs an empty string,
            *   we show an error and exit.
            */

            String key = JOptionPane.showInputDialog(frame, "Enter key of password to delete: ", "Info", JOptionPane.INFORMATION_MESSAGE);
            if(key == null || key.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Key cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            /*
            * Now, check if the entered key actually exists in the password store.
            * - If it exists: call deleteEntry() to remove the password.
            * - If not: inform the user that no such key exists.
            */

            if(passwordStore.getAllEntries().containsKey(key)) {
                passwordStore.deleteEntry(key);
                JOptionPane.showMessageDialog(frame, "Password deleted for key: " + key, "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "No password found for key: " + key, "Error", JOptionPane.ERROR_MESSAGE);
            }
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
            // Exit early if there are no passwords to show
            if (passwordStore.getAllEntries().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No passwords stored yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Create a search field where users can filter passwords by key (e.g., "Google", "GitHub")
            JTextField searchField = new JTextField(20);

            // Top panel that holds the label and the search field
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(new javax.swing.JLabel("Search: "), BorderLayout.WEST);
            topPanel.add(searchField, BorderLayout.CENTER);

            // Text area to display passwords (wrapped in a scroll pane for scrollability)
            JTextArea passwordArea = new JTextArea(15, 30);
            passwordArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(passwordArea);

            // Toggle button that allows the user to choose between showing or hiding passwords
            JToggleButton toggleButton = new JToggleButton("Show Passwords");

            // Combine all components into a main panel using BorderLayout
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(topPanel, BorderLayout.NORTH);     // Search bar at the top
            panel.add(scrollPane, BorderLayout.CENTER);  // Scrollable text area in the center
            panel.add(toggleButton, BorderLayout.SOUTH); // Show/hide toggle at the bottom

            /* This Runnable dynamically updates the password list display in the UI.
             * It works by:
             * - Reading the current text from the search bar to filter matching keys.
             * - Looping through all stored passwords and comparing keys case-insensitively.
             * - Showing the actual decrypted password only if the toggle is enabled; otherwise, it masks them.
             * - Finally, it updates the JTextArea with the filtered and formatted results.
             */

            Runnable updatePasswords = () -> {
                StringBuilder filteredPasswords = new StringBuilder("Stored Passwords:\n");

                String searchText = searchField.getText().trim().toLowerCase(); // Normalize input for comparison

                // Iterate through all entries and filter by search key
                for (Entry<String, String> entry : passwordStore.getAllEntries().entrySet()) {
                    String key = entry.getKey().toLowerCase(); // Normalize key
                    if (key.contains(searchText)) { // Match substring
                        String password = toggleButton.isSelected()
                            ? passwordStore.decrypt(entry.getValue()) // Decrypt only if user wants to see it
                            : "********"; // Otherwise mask it
                        filteredPasswords.append(entry.getKey()).append(": ").append(password).append("\n");
                    }
                }

                passwordArea.setText(filteredPasswords.toString()); // Update the text area
            };

            /* Attach a listener to the search field so every time the text changes (typed, deleted, modified),
             the password list is automatically refreshed by calling the updatePasswords runnable.
             */

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

            // Initialize the display with all passwords shown (or hidden if toggle is off)
            updatePasswords.run();

            // Handle toggle button press to switch between showing and hiding passwords
            toggleButton.addActionListener(e2 -> {
                toggleButton.setText(toggleButton.isSelected() ? "Hide Passwords" : "Show Passwords");
                updatePasswords.run(); // Refresh the list with the new visibility setting
            });

            // Show the entire assembled panel inside a JOptionPane
            JOptionPane.showMessageDialog(frame, panel, "View Passwords", JOptionPane.INFORMATION_MESSAGE);
        });

        /*
        * Adds an action listener to the "Export Passwords" button.
        *
        * When clicked:
        * - Checks if any passwords are stored; if none, informs the user and exits.
        * - Prompts the user to choose an export format: Plain Text or Encrypted.
        * - Opens a file save dialog (JFileChooser) for the user to select the destination file.
        * - Based on the chosen format, calls the appropriate method to export passwords.
        * - Displays a confirmation message upon successful export or cancellation.
        */
        exportButton.addActionListener(e -> {

            // If no passwords are stored, inform the user and exit early
            if (passwordStore.getAllEntries().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No passwords stored yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Prompt the user to choose the format they want to export in
            Object[] options = {"Plain Text", "Encrypted"};
            int choice = JOptionPane.showOptionDialog(
                frame,
                "Choose export format: ",
                "Export Format",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]  // Default option is "Plain Text"
            );

            // If the user closes the dialog without making a choice, cancel the export
            if (choice == JOptionPane.CLOSED_OPTION) {
                JOptionPane.showMessageDialog(frame, "Export cancelled.", "Export Cancelled", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Use JFileChooser to open a "Save As" dialog so the user can specify the destination file
            JFileChooser fileChooser = new JFileChooser();
            int userSelection = fileChooser.showSaveDialog(frame);

            // If the user selects a file and confirms
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                // Get the absolute path of the selected file
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();

                // Call the appropriate export method depending on the user's format choice
                if (choice == 0) { // Plain Text
                    passwordStore.exportToFilePlain(filePath);
                } else { // Encrypted
                    passwordStore.exportToFileEncrypted(filePath);
                }

                // Show success message with the export path
                JOptionPane.showMessageDialog(frame, "Passwords exported successfully to " + filePath, "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // If the user cancels the file selection
                JOptionPane.showMessageDialog(frame, "Export cancelled.", "Export Cancelled", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        /*
        * Adds an action listener to the "Import Passwords" button.
        *
        * When clicked:
        * - Opens a file chooser dialog (JFileChooser) to let the user select a file to import from.
        * - If a file is selected and approved:
        *   - The full file path is retrieved.
        *   - The passwords are loaded using the importFromFile() method.
        *   - A success message is shown to the user.
        * - If the user cancels or closes the dialog, a cancellation message is shown instead.
        */
        importButton.addActionListener(e -> {
            // Create a file chooser dialog for opening a file
            JFileChooser fileChooser = new JFileChooser();

            // Show the "Open" dialog and store the result (approve/cancel)
            int userSelection = fileChooser.showOpenDialog(frame);

            // If the user selected a file and clicked "Open"
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                // Retrieve the full path of the selected file
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();

                // Import password entries from the chosen file
                passwordStore.importFromFile(filePath);

                // Notify the user that the import was successful
                JOptionPane.showMessageDialog(
                    frame,
                    "Passwords imported successfully from " + filePath,
                    "Import Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                // If the user cancelled the dialog, inform them that the import was cancelled
                JOptionPane.showMessageDialog(
                    frame,
                    "Import cancelled.",
                    "Import Cancelled",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });


        /*
        * Adds an action listener to the "Edit Passwords" button.
        *
        * When clicked:
        * - Checks if any passwords are stored; if none, informs the user and exits.
        * - Displays a dialog with:
        *   - A search field to filter existing keys.
        *   - A list (JList) showing the filtered keys.
        *   - An "Edit Selected Password" button.
        * - Allows the user to select a key and edit its associated password.
        *   - Prompts for the new password and confirmation.
        *   - Validates the new password (non-empty, matching, strong).
        *   - Updates the password in the store upon successful validation.
        *   - Shows a confirmation message.
        */
        editPasswordsButton.addActionListener(e -> {
            // Create a text field for searching keys
            JTextField searchField = new JTextField(20);

            // Create a list model and JList to display keys
            DefaultListModel<String> listModel = new DefaultListModel<>();
            JList<String> entryList = new JList<>(listModel);
            JScrollPane scrollPane = new JScrollPane(entryList);

            // Create a button to initiate editing the selected password
            JButton editButton = new JButton("Edit Selected Password");

            // Assemble the top panel with the search label and field
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(new JLabel("Search: "), BorderLayout.WEST);
            topPanel.add(searchField, BorderLayout.CENTER);

            // Assemble the main panel with the top panel, list, and edit button
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(topPanel, BorderLayout.NORTH);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
            mainPanel.add(editButton, BorderLayout.SOUTH);

            // Populate the list model with all existing keys
            passwordStore.getAllEntries().keySet().forEach(listModel::addElement);

            // Add a document listener to the search field to filter the list dynamically
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                private void updateList() {
                    String searchText = searchField.getText().trim().toLowerCase();
                    listModel.clear();
                    for (String key : passwordStore.getAllEntries().keySet()) {
                        if (key.toLowerCase().contains(searchText)) {
                            listModel.addElement(key);
                        }
                    }
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateList();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateList();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateList();
                }
            });

            // If no passwords are stored, inform the user and exit
            if (passwordStore.getAllEntries().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No passwords stored yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Show the dialog with the main panel
            int dialogResult = JOptionPane.showConfirmDialog(frame, mainPanel, "Edit Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            // If the user clicked OK and selected a key from the list
            if (dialogResult == JOptionPane.OK_OPTION && entryList.getSelectedValue() != null) {
                String key = entryList.getSelectedValue();
                String newPassword;

                // Loop to prompt for the new password until valid input is provided
                while (true) {
                    // Create password fields for entering and confirming the new password
                    JPasswordField passwordField = new JPasswordField(20);
                    JPasswordField passwordField2 = new JPasswordField(20);

                    // Assemble the password input panel
                    JPanel passwordPanel = new JPanel(new GridLayout(2, 2));
                    passwordPanel.add(new JLabel("Enter password:"));
                    passwordPanel.add(passwordField);
                    passwordPanel.add(new JLabel("Re-enter password:"));
                    passwordPanel.add(passwordField2);

                    // Show the password input dialog
                    int result = JOptionPane.showConfirmDialog(frame, passwordPanel, "Enter password for key: " + key, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    // If the user cancels the dialog, exit
                    if (result != JOptionPane.OK_OPTION) {
                        return;
                    }

                    // Retrieve the entered passwords
                    newPassword = new String(passwordField.getPassword());
                    String password2 = new String(passwordField2.getPassword());

                    // Validate the new password
                    if (newPassword.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (!newPassword.equals(password2)) {
                        JOptionPane.showMessageDialog(frame, "Passwords do not match. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    } else if (!passwordStore.isPasswordStrong(newPassword)) {
                        JOptionPane.showMessageDialog(frame, "Password is not strong enough. It must be at least 8 characters long, contain uppercase and lowercase letters, numbers, and special characters.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // If all validations pass, break the loop
                        break;
                    }
                }

                // Update the password in the store
                passwordStore.addEntry(key, newPassword);

                // Inform the user that the password has been updated
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