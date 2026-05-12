import javax.swing.*;
import java.awt.*;

public class UserManagementFrame extends JFrame {

    public UserManagementFrame(String username, boolean isAdmin) {
        setTitle("User Management");
        setSize(450, 360);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.add(buildNav(username, isAdmin), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("User Management", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        form.add(title, gbc);

        ButtonGroup userTypeGroup = new ButtonGroup();
        JRadioButton newUserRB = new JRadioButton("New User", true);
        JRadioButton existingRB = new JRadioButton("Existing User");
        userTypeGroup.add(newUserRB);
        userTypeGroup.add(existingRB);

        JPanel typePanel = new JPanel(new FlowLayout());
        typePanel.add(newUserRB);
        typePanel.add(existingRB);
        gbc.gridy = 1;
        form.add(typePanel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        form.add(new JLabel("Username *:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        form.add(nameField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        form.add(new JLabel("Password *:"), gbc);
        gbc.gridx = 1;
        JPasswordField passField = new JPasswordField(15);
        form.add(passField, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JCheckBox activeChk = new JCheckBox("Active", true);
        form.add(activeChk, gbc);

        gbc.gridy = 5;
        JCheckBox adminChk = new JCheckBox("Admin");
        form.add(adminChk, gbc);

        JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        gbc.gridy = 6;
        form.add(errorLabel, gbc);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        JPanel btnPanel = new JPanel();
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        gbc.gridy = 7;
        form.add(btnPanel, gbc);

        saveBtn.addActionListener(e -> {
            String uname = nameField.getText().trim();
            String pass = new String(passField.getPassword());

            if (uname.isEmpty()) {
                errorLabel.setText("Username is required.");
                return;
            }
            if (newUserRB.isSelected() && pass.isEmpty()) {
                errorLabel.setText("Password is required for new user.");
                return;
            }
            if (newUserRB.isSelected() && DataStore.users.containsKey(uname)) {
                errorLabel.setText("User already exists.");
                return;
            }
            if (existingRB.isSelected() && !DataStore.users.containsKey(uname)) {
                errorLabel.setText("User not found.");
                return;
            }

            String isAdminStr = adminChk.isSelected() ? "true" : "false";
            String finalPass = pass.isEmpty() ? DataStore.users.get(uname)[0] : pass;
            DataStore.users.put(uname, new String[]{finalPass, isAdminStr});

            JOptionPane.showMessageDialog(null, "User saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new MaintenanceFrame(username, isAdmin).setVisible(true);
        });

        cancelBtn.addActionListener(e -> {
            dispose();
            new MaintenanceFrame(username, isAdmin).setVisible(true);
        });

        main.add(form, BorderLayout.CENTER);
        add(main);
    }

    private JPanel buildNav(String username, boolean isAdmin) {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton homeBtn = new JButton("Home");
        JButton logoutBtn = new JButton("Log Out");
        nav.add(homeBtn);
        nav.add(logoutBtn);

        homeBtn.addActionListener(e -> {
            dispose();
            new AdminHomeFrame(username).setVisible(true);
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        return nav;
    }
}