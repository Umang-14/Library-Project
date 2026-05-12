import javax.swing.*;
import java.awt.*;

public class AddMembershipFrame extends JFrame {

    public AddMembershipFrame(String username, boolean isAdmin) {
        setTitle("Add Membership");
        setSize(500, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.add(buildNav(username, isAdmin), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Add Membership", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        form.add(title, gbc);

        gbc.gridwidth = 1;
        String[] fieldNames = {"First Name *:", "Last Name *:", "Contact Number *:", "Contact Address *:", "Aadhar Card No *:"};
        JTextField[] fields = new JTextField[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            gbc.gridy = i + 1; gbc.gridx = 0;
            form.add(new JLabel(fieldNames[i]), gbc);
            gbc.gridx = 1;
            fields[i] = new JTextField(15);
            form.add(fields[i], gbc);
        }

        gbc.gridy = 6; gbc.gridx = 0;
        form.add(new JLabel("Start Date *:"), gbc);
        gbc.gridx = 1;
        JTextField startDate = new JTextField(DataStore.getTodayDate());
        form.add(startDate, gbc);

        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 2;
        form.add(new JLabel("Membership Duration:"), gbc);

        ButtonGroup durGroup = new ButtonGroup();
        JRadioButton sixMonths = new JRadioButton("Six Months", true);
        JRadioButton oneYear = new JRadioButton("One Year");
        JRadioButton twoYears = new JRadioButton("Two Years");
        durGroup.add(sixMonths); durGroup.add(oneYear); durGroup.add(twoYears);
        JPanel radioPanel = new JPanel(new FlowLayout());
        radioPanel.add(sixMonths); radioPanel.add(oneYear); radioPanel.add(twoYears);
        gbc.gridy = 8;
        form.add(radioPanel, gbc);

        JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        gbc.gridy = 9;
        form.add(errorLabel, gbc);

        JButton confirmBtn = new JButton("Add Membership");
        JButton cancelBtn = new JButton("Cancel");
        JPanel btnPanel = new JPanel();
        btnPanel.add(confirmBtn);
        btnPanel.add(cancelBtn);
        gbc.gridy = 10;
        form.add(btnPanel, gbc);

        confirmBtn.addActionListener(e -> {
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getText().trim().isEmpty()) {
                    errorLabel.setText("All fields are required.");
                    return;
                }
            }
            if (startDate.getText().trim().isEmpty()) {
                errorLabel.setText("Start Date is required.");
                return;
            }
            int months = sixMonths.isSelected() ? 6 : oneYear.isSelected() ? 12 : 24;
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String endDateStr;
            try {
                java.time.LocalDate sd = java.time.LocalDate.parse(startDate.getText().trim(), fmt);
                endDateStr = sd.plusMonths(months).format(fmt);
            } catch (Exception ex) {
                endDateStr = "N/A";
            }
            String memId = "MEM" + String.format("%03d", DataStore.memberships.size() + 1);
            DataStore.memberships.add(new String[]{memId, fields[0].getText(), fields[1].getText(), fields[2].getText(), fields[3].getText(), fields[4].getText(), startDate.getText(), endDateStr, "Active", "0"});
            JOptionPane.showMessageDialog(null, "Membership added! ID: " + memId, "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new MaintenanceFrame(username, isAdmin).setVisible(true);
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