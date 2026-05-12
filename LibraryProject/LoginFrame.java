import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private JTextField userField;
    private JPasswordField passField;
    private JLabel errorLabel;

    public LoginFrame() {
        setTitle("Library Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Library Management System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("User ID:"), gbc);
        gbc.gridx = 1;
        userField = new JTextField(15);
        panel.add(userField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passField = new JPasswordField(15);
        panel.add(passField, gbc);

        JButton loginBtn = new JButton("Login");
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);

        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        gbc.gridy = 4;
        panel.add(errorLabel, gbc);

        loginBtn.addActionListener(e -> doLogin());
        passField.addActionListener(e -> doLogin()); // enter dabao toh bhi login ho

        add(panel);
    }

    private void doLogin() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter User ID and Password.");
            return;
        }

        if (DataStore.validateLogin(username, password)) {
            Session.currentUsername = username;
            Session.isAdmin = DataStore.isAdmin(username);
            dispose();

            if (Session.isAdmin) {
                new AdminHomeFrame(username).setVisible(true);
            } else {
                new UserHomeFrame(username).setVisible(true);
            }
        } else {
            errorLabel.setText("Invalid User ID or Password.");
            passField.setText("");
        }
    }
}