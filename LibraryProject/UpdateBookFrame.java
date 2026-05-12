import javax.swing.*;
import java.awt.*;

public class UpdateBookFrame extends JFrame {

    public UpdateBookFrame(String username, boolean isAdmin) {
        setTitle("Update Book/Movie");
        setSize(450, 380);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.add(buildNav(username, isAdmin), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Update Book/Movie", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        form.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        form.add(new JLabel("Book/Movie Name *:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        form.add(nameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        form.add(new JLabel("Serial No *:"), gbc);
        gbc.gridx = 1;
        JTextField serialField = new JTextField(15);
        form.add(serialField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        form.add(new JLabel("Status *:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Available", "Issued", "Lost", "Damaged"});
        form.add(statusCombo, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        form.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        JTextField dateField = new JTextField(DataStore.getTodayDate());
        form.add(dateField, gbc);

        JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        form.add(errorLabel, gbc);

        JButton updateBtn = new JButton("Update");
        JButton cancelBtn = new JButton("Cancel");
        JPanel btnPanel = new JPanel();
        btnPanel.add(updateBtn);
        btnPanel.add(cancelBtn);
        gbc.gridy = 6;
        form.add(btnPanel, gbc);

        updateBtn.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty() && serialField.getText().trim().isEmpty()) {
                errorLabel.setText("Enter at least Name or Serial No.");
                return;
            }
            boolean found = false;
            for (String[] b : DataStore.books) {
                if (b[1].equalsIgnoreCase(nameField.getText().trim()) ||
                    b[0].equalsIgnoreCase(serialField.getText().trim())) {
                    b[4] = (String) statusCombo.getSelectedItem();
                    found = true;
                    break;
                }
            }
            if (!found) {
                errorLabel.setText("Book/Movie not found.");
                return;
            }
            JOptionPane.showMessageDialog(null, "Updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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