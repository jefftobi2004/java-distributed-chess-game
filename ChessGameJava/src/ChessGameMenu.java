import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessGameMenu extends JFrame {

    public ChessGameMenu() {
        setTitle("Multiplayer Chess Game - Main Menu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1500, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        JLabel imageLabel = new JLabel();

        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/Chess3.jpg"));
        Image img = icon.getImage().getScaledInstance(900, 700, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(img));
        leftPanel.add(imageLabel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        Color brownBackground = new Color(102, 51, 0);
        rightPanel.setBackground(brownBackground);

        JPanel buttonBox = new JPanel();
        buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.Y_AXIS));
        buttonBox.setOpaque(false);

        String[] buttonLabels = {
                "Play Game", "Replays", "Leaderboard", "Switch Account", "Exit"
        };

        Color skinTone = new Color(255, 224, 189);
        Color fontColor = new Color(60, 30, 10);

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(350, 60)); // Larger buttons
            button.setPreferredSize(new Dimension(350, 60));
            button.setFocusPainted(false);
            button.setBackground(skinTone);
            button.setForeground(fontColor);
            button.setFont(new Font("SansSerif", Font.BOLD, 18));
            button.setBorder(BorderFactory.createLineBorder(fontColor, 2));

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (label) {
                        case "Exit":
                            System.exit(0);
                            break;
                        case "Switch Account":
                            dispose();
                            new ChessGameLogin();
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, label + " clicked!");
                    }
                }
            });

            buttonBox.add(Box.createVerticalStrut(30));
            buttonBox.add(button);
        }

        rightPanel.add(buttonBox, new GridBagConstraints());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(900);
        splitPane.setEnabled(false);
        add(splitPane);

        setVisible(true);
    }

    public static void main(String[] args) {
        new ChessGameMenu();
    }
}
