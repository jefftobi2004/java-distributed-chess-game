import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ChessGameSignup extends JFrame {

    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    private JButton backButton;
    private JButton signUpButton;

    public ChessGameSignup() {
        setTitle("Multiplayer Chess Game - Register");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(27, 153, 139));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel("Sign Up Form");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        mainPanel.add(titleLabel);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 20, 20));
        formPanel.setBackground(new Color(27, 153, 139));

        Font labelFont = new Font("SansSerif", Font.PLAIN, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);

        // Name
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        nameLabel.setForeground(Color.WHITE);
        usernameField = new JTextField();
        usernameField.setFont(fieldFont);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        emailLabel.setForeground(Color.WHITE);
        emailField = new JTextField();
        emailField.setFont(fieldFont);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(Color.WHITE);
        passwordField = new JPasswordField();
        passwordField.setFont(fieldFont);

        //Buttons
        backButton = new JButton("Back to Login");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.setForeground(new Color(27, 153, 139));
        backButton.setBackground(Color.WHITE);
        backButton.setFocusPainted(false);

        signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setBackground(new Color(27, 153, 139));
        signUpButton.setFocusPainted(false);

        formPanel.add(nameLabel);       formPanel.add(usernameField);
        formPanel.add(emailLabel);      formPanel.add(emailField);
        formPanel.add(passwordLabel);   formPanel.add(passwordField);
        formPanel.add(backButton);      formPanel.add(signUpButton);

        mainPanel.add(formPanel);

        add(mainPanel);
        setVisible(true);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                //new ChessGameLogin();
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signupClient();
            }
        });
    }

    private void signupClient() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if(username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields !");
            return;
        }

        if(checkExistingEmail(email)) {
            JOptionPane.showMessageDialog(this, "This email is already registered !");
            return;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format!");
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password should be at least 6 characters long.");
            return;
        }

        registerClientData(username, email, password);

    }

    private void registerClientData(String username, String email, String password) {

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, hashedPassword);

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Please try again !");
            }

            preparedStatement.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkExistingEmail(String email) {

        boolean result = false;

        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                result = true;
            }

            resultSet.close();
            stmt.close();
            conn.close();

            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new ChessGameSignup();
    }
}
