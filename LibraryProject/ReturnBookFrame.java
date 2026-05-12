import javax.swing.*;
import java.awt.*;

public class ReturnBookFrame extends JFrame {

    private String username;
    private boolean isAdmin;

    public ReturnBookFrame(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;

        setTitle("Return Book");
        setSize(500, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.add(buildNav(), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Return Book", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        form.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        form.add(new JLabel("Book Name *:"), gbc);
        gbc.gridx = 1;

        String[] issuedBooks = DataStore.activeIssues.stream()
            .map(i -> i[1])
            .distinct()
            .toArray(String[]::new);
        String[] options = new String[issuedBooks.length + 1];
        options[0] = "";
        System.arraycopy(issuedBooks, 0, options, 1, issuedBooks.length);
        JComboBox<String> bookCombo = new JComboBox<>(options);
        form.add(bookCombo, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        form.add(new JLabel("Author (auto):"), gbc);
        gbc.gridx = 1;
        JTextField authorField = new JTextField();
        authorField.setEditable(false);
        authorField.setBackground(Color.LIGHT_GRAY);
        form.add(authorField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        form.add(new JLabel("Serial No *:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> serialCombo = new JComboBox<>(new String[]{""});
        form.add(serialCombo, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        form.add(new JLabel("Issue Date (auto):"), gbc);
        gbc.gridx = 1;
        JTextField issueDateField = new JTextField();
        issueDateField.setEditable(false);
        issueDateField.setBackground(Color.LIGHT_GRAY);
        form.add(issueDateField, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        form.add(new JLabel("Return Date *:"), gbc);
        gbc.gridx = 1;
        JTextField returnDateField = new JTextField();
        form.add(returnDateField, gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        form.add(new JLabel("Remarks:"), gbc);
        gbc.gridx = 1;
        JTextArea remarksArea = new JTextArea(2, 15);
        form.add(new JScrollPane(remarksArea), gbc);

        JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        form.add(errorLabel, gbc);

        JButton confirmBtn = new JButton("Confirm");
        JButton cancelBtn = new JButton("Cancel");
        JPanel btnPanel = new JPanel();
        btnPanel.add(confirmBtn);
        btnPanel.add(cancelBtn);
        gbc.gridy = 8;
        form.add(btnPanel, gbc);

        bookCombo.addActionListener(e -> {
            String selected = (String) bookCombo.getSelectedItem();
            serialCombo.removeAllItems();
            serialCombo.addItem("");
            if (selected != null && !selected.isEmpty()) {
                String[] book = DataStore.getBookByName(selected);
                if (book != null)
                    authorField.setText(book[2]);
                for (String[] issue : DataStore.activeIssues) {
                    if (issue[1].equals(selected)) {
                        serialCombo.addItem(issue[0]);
                        issueDateField.setText(issue[3]);
                        returnDateField.setText(issue[4]);
                    }
                }
            }
        });

        confirmBtn.addActionListener(e -> {
            String bookName = (String) bookCombo.getSelectedItem();
            String serial = (String) serialCombo.getSelectedItem();
            String retDate = returnDateField.getText().trim();
            if (bookName == null || bookName.isEmpty() || serial == null || serial.isEmpty() || retDate.isEmpty()) {
                errorLabel.setText("Book Name, Serial No and Return Date are required.");
                return;
            }
            dispose();
            new PayFineFrame(username, isAdmin, bookName, serial, issueDateField.getText(), retDate).setVisible(true);
        });

        cancelBtn.addActionListener(e -> {
            dispose();
            new TransactionsFrame(username, isAdmin).setVisible(true);
        });

        main.add(form, BorderLayout.CENTER);
        add(main);
    }

    private JPanel buildNav() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton transBtn = new JButton("Transactions");
        JButton homeBtn = new JButton("Home");
        JButton logoutBtn = new JButton("Log Out");
        nav.add(transBtn);
        nav.add(homeBtn);
        nav.add(logoutBtn);
        transBtn.addActionListener(e -> {
            dispose();
            new TransactionsFrame(username, isAdmin).setVisible(true);
        });
        homeBtn.addActionListener(e -> {
            dispose();
            if (isAdmin)
                new AdminHomeFrame(username).setVisible(true);
            else
                new UserHomeFrame(username).setVisible(true);
        });
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        return nav;
    }
}