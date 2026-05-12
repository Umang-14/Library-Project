import javax.swing.*;
import java.awt.*;

public class UpdateMembershipFrame extends JFrame {

    public UpdateMembershipFrame(String username, boolean isAdmin) {
        setTitle("Update Membership");
        setSize(500, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.add(buildNav(username, isAdmin), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Update Membership", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        form.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        form.add(new JLabel("Membership Number *:"), gbc);
        gbc.gridx = 1;
        JTextField memNumField = new JTextField(15);
        form.add(memNumField, gbc);

        JTextField startDate = new JTextField();
        JTextField endDate = new JTextField();
        startDate.setEditable(false); startDate.setBackground(Color.LIGHT_GRAY);
        endDate.setEditable(false); endDate.setBackground(Color.LIGHT_GRAY);

        JButton loadBtn = new JButton("Load");
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        form.add(loadBtn, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 3; gbc.gridx = 0;
        form.add(new JLabel("Start Date:"), gbc);
        gbc.gridx = 1;
        form.add(startDate, gbc);

        gbc.gridy = 4; gbc.gridx = 0;
        form.add(new JLabel("End Date:"), gbc);
        gbc.gridx = 1;
        form.add(endDate, gbc);

        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        form.add(new JLabel("Extend By:"), gbc);

        ButtonGroup extGroup = new ButtonGroup();
        JRadioButton sixMonths = new JRadioButton("Six Months", true);
        JRadioButton oneYear = new JRadioButton("One Year");
        JRadioButton twoYears = new JRadioButton("Two Years");
        extGroup.add(sixMonths); extGroup.add(oneYear); extGroup.add(twoYears);
        JPanel extPanel = new JPanel(new FlowLayout());
        extPanel.add(sixMonths); extPanel.add(oneYear); extPanel.add(twoYears);
        gbc.gridy = 6;
        form.add(extPanel, gbc);

        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 2;
        JRadioButton removeRB = new JRadioButton("Remove Membership");
        form.add(removeRB, gbc);

        JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        gbc.gridy = 8;
        form.add(errorLabel, gbc);

        JButton confirmBtn = new JButton("Confirm");
        JButton cancelBtn = new JButton("Cancel");
        JPanel btnPanel = new JPanel();
        btnPanel.add(confirmBtn);
        btnPanel.add(cancelBtn);
        gbc.gridy = 9;
        form.add(btnPanel, gbc);

        loadBtn.addActionListener(e -> {
            String id = memNumField.getText().trim();
            boolean found = false;
            for (String[] m : DataStore.memberships) {
                if (m[0].equalsIgnoreCase(id)) {
                    startDate.setText(m[6]);
                    endDate.setText(m[7]);
                    found = true;
                    break;
                }
            }
            if (!found) errorLabel.setText("Membership not found.");
            else errorLabel.setText("");
        });

        confirmBtn.addActionListener(e -> {
            String id = memNumField.getText().trim();
            if (id.isEmpty()) { errorLabel.setText("Membership Number required."); return; }
            for (String[] m : DataStore.memberships) {
                if (m[0].equalsIgnoreCase(id)) {
                    if (removeRB.isSelected()) {
                        m[8] = "Inactive";
                        JOptionPane.showMessageDialog(null, "Membership removed.", "Done", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        int months = sixMonths.isSelected() ? 6 : oneYear.isSelected() ? 12 : 24;
                        try {
                            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            java.time.LocalDate ed = java.time.LocalDate.parse(m[7], fmt);
                            m[7] = ed.plusMonths(months).format(fmt);
                        } catch (Exception ex) { /* keep same */ }
                        JOptionPane.showMessageDialog(null, "Membership extended.", "Done", JOptionPane.INFORMATION_MESSAGE);
                    }
                    dispose();
                    new MaintenanceFrame(username, isAdmin).setVisible(true);
                    return;
                }
            }
            errorLabel.setText("Membership not found.");
        });
        cancelBtn.addActionListener(e -> { dispose(); new MaintenanceFrame(username, isAdmin).setVisible(true); });

        main.add(form, BorderLayout.CENTER);
        add(main);
    }

    private JPanel buildNav(String username, boolean isAdmin) {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton homeBtn = new JButton("Home");
        JButton logoutBtn = new JButton("Log Out");
        nav.add(homeBtn);
        nav.add(logoutBtn);
        homeBtn.addActionListener(e -> { dispose(); new AdminHomeFrame(username).setVisible(true); });
        logoutBtn.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        return nav;
    }
}