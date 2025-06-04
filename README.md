# Password Manager

This is a basic Java desktop password manager application. It uses JavaFX for the user interface and SQLite as the backend database to securely store user credentials.

## Features
- Add, view, and manage passwords
- Secure storage using SQLite
- Simple and modern JavaFX UI

## Requirements
- Java 11 or higher
- Maven

## How to Run
1. Build the project with Maven:
   ```powershell
   mvn clean install
   ```
2. Run the application:
   ```powershell
   mvn exec:java -Dexec.mainClass="com.passwordmanager.App"
   ```

## Notes
- Do not share your master password or sensitive data.
- This is a basic demo and should not be used for production without further security enhancements.
