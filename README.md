# 🔐 Java Password Manager

A secure and user-friendly desktop application built with Java Swing. This project allows you to manage passwords locally with encryption, offering add, view, edit, delete, import, and export functionalities.

## 📦 Features

- **Add Password**: Enter a unique key (e.g., "Gmail") and a strong password (validated for security).
- **View Passwords**: Display stored passwords with a search bar and a toggle to show/hide actual values.
- **Edit Password**: Search and select a key to update its password securely.
- **Delete Password**: Remove a password entry using its key.
- **Import/Export**: Load and save passwords from/to plain or encrypted files.

## 🚀 Getting Started

1. **Compile the files**:
   ```bash
   javac Main.java PasswordStore.java
   ```

2. **Run the program**:
   ```bash
   java Main
   ```

## 🛠 Technologies

- Java SE
- Java Swing for GUI
- AES encryption for password security

## 💡 What I Learned

### `StringBuilder`
Used to efficiently build dynamic strings, especially when concatenating multiple values for password display.

### `Runnable`
Encapsulates a block of code (a task) to be executed. Used here to update the password display whenever the search field changes.

### `DefaultListModel`
A model for managing dynamic data in JList. Allows us to add/remove items in real time when filtering keys for editing.

### File Handling
Learned how to use `JFileChooser` to let users select files for import/export and how to read/write files using `BufferedReader` and `BufferedWriter`.

## 📁 Project Structure

```
├── Main.java            // Handles GUI and user interactions
├── PasswordStore.java   // Stores passwords, handles encryption and file I/O
└── README.md            // Project documentation
```

## 🧾 License

This project is open-source under the MIT License.
