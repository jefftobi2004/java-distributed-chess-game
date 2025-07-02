# Java Chess Game with User Authentication

This project is a desktop chess game built with Java Swing, featuring a graphical interface for playing chess, and a login system to authenticate users before they start a game. User passwords are securely stored in a database using modern encryption techniques.

## Project Idea

The goal of the project is to recreate a classic chess game with a user-friendly interface, while also practicing secure authentication methods. Players must log in or register before playing. Once authenticated, they can play a full game of chess with correct piece movements, turn-based logic, and pawn promotion. The project is meant as a combination of learning object-oriented programming, building GUIs with Java Swing, working with databases, and implementing basic cybersecurity best practices.

## Technologies Used

- **Java Swing** – for building the GUI and handling events.
- **JDBC** – to connect to a MySQL database for storing user accounts and scores.
- **BCrypt / jBCrypt** – for secure password hashing.
- **MySQL** – for the user database backend.
- **Object-Oriented Design** – for representing chess pieces, cells, and game logic with clear class hierarchies.

## Bibliography & Resources

- **Password Encryption Library (jBCrypt)**  
  [jBCrypt Download Site](https://www.mindrot.org/projects/jBCrypt/))  
  Used for securely hashing user passwords before storing them in the database.

-  **Chess Tutorials for Move Rules and Game Mechanics**  
  [Chess.com Rules Guide](https://www.chess.com/learn-how-to-play-chess)  
  Helped with implementing the correct movement rules, pawn promotion, and check/checkmate detection.

-  **JDBC Used (Java Database Connectivity)**  
  [MySQL Connector](https://dev.mysql.com/downloads/connector/j/)  
  Used to integrate MySQL database access into the Java application.

-  **Java Swing Documentation**  
  [Official Java Swing Docs](https://docs.oracle.com/javase/8/docs/api/javax/swing/package-summary.html)  
  Reference for GUI components, dialogs, and event handling.

## Features

- User registration and login with encrypted password storage.
- Interactive chessboard with move validation.
- Visual feedback on selected pieces and available moves.
- Turn-based logic for white and black players.
- Pawn promotion dialog to choose a replacement piece.
- Score tracking and user data stored in MySQL.


