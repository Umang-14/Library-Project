import javax.swing.*;
import java.awt.*;

public class AddBookFrame extends JFrame {

    public AddBookFrame(String username, boolean isAdmin) {
        setTitle("Add Book/Movie");
        setSize(450, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.add(buildNav(username, isAdmin), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Add Book/Movie", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        form.add(title, gbc);

        ButtonGroup typeGroup = new ButtonGroup();
        JRadioButton bookRB = new JRadioButton("Book", true);
        JRadioButton movieRB = new JRadioButton("Movie");
        typeGroup.add(bookRB); typeGroup.add(movieRB);
        JPanel typePanel = new JPanel(new FlowLayout());
        typePanel.add(bookRB); typePanel.add(movieRB);
        gbc.gridy = 1;
        form.add(typePanel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2; gbc.gridx = 0;
        form.add(new JLabel("Name *:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        form.add(nameField, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        form.add(new JLabel("Author/Director *:"), gbc);
        gbc.gridx = 1;
        JTextField authorField = new JTextField(15);
        form.add(authorField, gbc);

        gbc.gridy = 4; gbc.gridx = 0;
        form.add(new JLabel("Category *:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> catCombo = new JComboBox<>(new String[]{"Science", "Economics", "Fiction", "Children", "Personal Development"});
        form.add(catCombo, gbc);

        gbc.gridy = 5; gbc.gridx = 0;
        form.add(new JLabel("Cost *:"), gbc);
        gbc.gridx = 1;
        JTextField costField = new JTextField(15);
        form.add(costField, gbc);

        gbc.gridy = 6; gbc.gridx = 0;
        form.add(new JLabel("Procurement Date *:"), gbc);
        gbc.gridx = 1;
        JTextField dateField = new JTextField(DataStore.getTodayDate());
        form.add(dateField, gbc);

        JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        gbc.gridy = 7; gbc.gridwidth = 2; gbc.gridx = 0;
        form.add(errorLabel, gbc);

        JButton addBtn = new JButton("Add");
        JButton cancelBtn = new JButton("Cancel");
        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn); btnPanel.add(cancelBtn);
        gbc.gridy = 8;
        form.add(btnPanel, gbc);

        addBtn.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty() || authorField.getText().trim().isEmpty() || costField.getText().trim().isEmpty()) {
                errorLabel.setText("All fields are required.");
                return;
            }
            String type = bookRB.isSelected() ? "Book" : "Movie";
            String prefix = type.equals("Book") ? "B" : "M";
            String cat = ((String) catCombo.getSelectedItem()).substring(0, 2).toUpperCase();
            String serial = cat + prefix + String.format("%06d", DataStore.books.size() + 1);
            DataStore.books.add(new String[]{serial, nameField.getText(), authorField.getText(), (String)catCombo.getSelectedItem(), "Available", costField.getText(), dateField.getText(), type});
            JOptionPane.showMessageDialog(null, type + " added! Serial: " + serial, "Success", JOptionPane.INFORMATION_MESSAGE);
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