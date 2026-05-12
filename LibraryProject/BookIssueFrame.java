import javax.swing.*;
import java.awt.*;

public class BookIssueFrame extends JFrame {

    public BookIssueFrame(String username, boolean isAdmin) {
        setTitle("Book Issue");
        setSize(500, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.add(buildNav(username, isAdmin), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Book Issue", SwingConstants.CENTER);
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

        // sirf available books dikhao
        String[] availableBooks = DataStore.books.stream()
            .filter(b -> b[4].equals("Available"))
            .map(b -> b[1])
            .toArray(String[]::new);
        String[] bookOptions = new String[availableBooks.length + 1];
        bookOptions[0] = "";
        System.arraycopy(availableBooks, 0, bookOptions, 1, availableBooks.length);
        JComboBox<String> bookCombo = new JComboBox<>(bookOptions);
        form.add(bookCombo, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        form.add(new JLabel("Author (auto):"), gbc);
        gbc.gridx = 1;
        JTextField authorField = new JTextField(15);
        authorField.setEditable(false);
        authorField.setBackground(Color.LIGHT_GRAY);
        form.add(authorField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        form.add(new JLabel("Membership ID *:"), gbc);
        gbc.gridx = 1;
        JTextField memberField = new JTextField(15);
        form.add(memberField, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        form.add(new JLabel("Issue Date *:"), gbc);
        gbc.gridx = 1;
        JTextField issueDateField = new JTextField(DataStore.getTodayDate());
        form.add(issueDateField, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        form.add(new JLabel("Return Date *:"), gbc);
        gbc.gridx = 1;
        JTextField returnDateField = new JTextField(DataStore.getReturnDate());
        form.add(returnDateField, gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        form.add(new JLabel("Remarks:"), gbc);
        gbc.gridx = 1;
        JTextArea remarksArea = new JTextArea(2, 15);
        remarksArea.setLineWrap(true);
        form.add(new JScrollPane(remarksArea), gbc);

        JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        form.add(errorLabel, gbc);

        JButton confirmBtn = new JButton("Confirm Issue");
        JButton cancelBtn = new JButton("Cancel");
        JPanel btnPanel = new JPanel();
        btnPanel.add(confirmBtn);
        btnPanel.add(cancelBtn);
        gbc.gridy = 8;
        form.add(btnPanel, gbc);

        bookCombo.addActionListener(e -> {
            String selected = (String) bookCombo.getSelectedItem();
            if (selected != null && !selected.isEmpty()) {
                String[] book = DataStore.getBookByName(selected);
                if (book != null)
                    authorField.setText(book[2]);
            } else {
                authorField.setText("");
            }
        });

        confirmBtn.addActionListener(e -> {
            String bookName = (String) bookCombo.getSelectedItem();
            String memberId = memberField.getText().trim();

            if (bookName == null || bookName.isEmpty()) {
                errorLabel.setText("Please select a book.");
                return;
            }
            if (memberId.isEmpty()) {
                errorLabel.setText("Membership ID is required.");
                return;
            }
            String issueDate = issueDateField.getText().trim();
            String returnDate = returnDateField.getText().trim();
            if (issueDate.isEmpty() || returnDate.isEmpty()) {
                errorLabel.setText("Issue Date and Return Date are required.");
                return;
            }

            for (String[] b : DataStore.books) {
                if (b[1].equals(bookName)) {
                    b[4] = "Issued";
                    DataStore.activeIssues.add(new String[]{b[0], bookName, memberId, issueDate, returnDate});
                    break;
                }
            }
            JOptionPane.showMessageDialog(null, "Book issued successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new TransactionsFrame(username, isAdmin).setVisible(true);
        });

        cancelBtn.addActionListener(e -> {
            dispose();
            new TransactionsFrame(username, isAdmin).setVisible(true);
        });

        main.add(form, BorderLayout.CENTER);
        add(main);
    }

    private JPanel buildNav(String username, boolean isAdmin) {
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