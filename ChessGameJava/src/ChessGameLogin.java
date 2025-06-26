import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ChessGameLogin extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton signUpButton;
    private JButton signInButton;

    public ChessGameLogin() {
        setTitle("Multiplayer Chess Game - Log In");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        // Realizam un split Panel

        //Panoul de stanga
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(27, 153, 139));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("<html><div align='center'>Welcome to <br><i>Multiplayer Chess Game</i></div></html>");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(60, 0, 20, 0));

        JLabel infoLabel = new JLabel("<html><div align = 'center'>Dont have an account yet? Please register down below!</div></html>");
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        signUpButton = new JButton("SIGN UP");
        signUpButton.setFocusPainted(false);
        signUpButton.setForeground(new Color(27, 153, 139));
        signUpButton.setBackground(Color.WHITE);
        signUpButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        signUpButton.setMaximumSize(new Dimension(200, 40));
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        leftPanel.add(welcomeLabel);
        leftPanel.add(infoLabel);
        leftPanel.add(signUpButton);

        // Panoul de dreapta
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JLabel createAccountLabel = new JLabel("<html><div align='center'>Log In</div></html>");
        createAccountLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        createAccountLabel.setForeground(new Color(27, 153, 139));
        createAccountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccountLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        emailLabel.setForeground(new Color(27, 153, 139));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel.setHorizontalAlignment(SwingConstants.LEFT);
        emailLabel.setMaximumSize(new Dimension(300, 20));

        emailField = new JTextField();
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        emailField.setMaximumSize(new Dimension(300, 40));
        emailField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        emailField.setBackground(new Color(238, 238, 238));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(27, 153, 139));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setHorizontalAlignment(SwingConstants.LEFT);
        passwordLabel.setMaximumSize(new Dimension(300, 20));

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordField.setMaximumSize(new Dimension(300, 40));
        passwordField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        passwordField.setBackground(new Color(238, 238, 238));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        signInButton = new JButton("SIGN IN");
        signInButton.setFocusPainted(false);
        signInButton.setForeground(Color.WHITE);
        signInButton.setBackground(new Color(27, 153, 139));
        signInButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        signInButton.setMaximumSize(new Dimension(300, 40));
        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // createVerticalStrut ajuta la stabilirea a unei spatieri verticale a elementelor ce urmeaza adaugat in panel
        rightPanel.add(Box.createVerticalStrut(30));
        rightPanel.add(createAccountLabel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(emailLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(emailField);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(passwordLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(passwordField);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(signInButton);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(errorLabel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);
        splitPane.setEnabled(false);
        add(splitPane);

        // Setam evenimentele ce au loc la interactiunea cu butoanele
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ChessGameSignup();
            }
        });

        setVisible(true);
    }

    private void login() {
        String email = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all your login information.");
            return;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT id_user, username, password FROM users WHERE email = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");

                if (BCrypt.checkpw(password, hashedPassword)) {
                    int userId = resultSet.getInt("id_user");
                    String username = resultSet.getString("username");

                    UserSession.startSession(userId, email, username);
                    JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + username + ".");

                    dispose();
                    //new ChessGameMenu();
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect password.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "This email address hasn't yet been registered.");
            }

            resultSet.close();
            preparedStatement.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ChessGameLogin();
    }
}
