import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BookAvailableFrame extends JFrame {

    public BookAvailableFrame(String username, boolean isAdmin) {
        setTitle("Book Availability");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.add(buildNav(username, isAdmin), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Book Availability", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        form.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        form.add(new JLabel("Enter Book Name:"), gbc);
        gbc.gridx = 1;

        String[] bookNames = DataStore.books.stream()
                .map(b -> b[1])
                .toArray(String[]::new);
        String[] nameOptions = new String[bookNames.length + 1];
        nameOptions[0] = "";
        System.arraycopy(bookNames, 0, nameOptions, 1, bookNames.length);
        JComboBox<String> nameCombo = new JComboBox<>(nameOptions);
        form.add(nameCombo, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        form.add(new JLabel("Enter Author:"), gbc);
        gbc.gridx = 1;

        String[] authors = DataStore.books.stream()
                .map(b -> b[2])
                .distinct()
                .toArray(String[]::new);
        String[] authorOptions = new String[authors.length + 1];
        authorOptions[0] = "";
        System.arraycopy(authors, 0, authorOptions, 1, authors.length);
        JComboBox<String> authorCombo = new JComboBox<>(authorOptions);
        form.add(authorCombo, gbc);

        JButton searchBtn = new JButton("Search");
        JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        form.add(searchBtn, gbc);
        gbc.gridy = 4;
        form.add(errorLabel, gbc);

        String[] cols = {"Book Name", "Author Name", "Serial Number", "Status"};
        JTable resultsTable = new JTable(new Object[0][4], cols);
        JScrollPane scroll = new JScrollPane(resultsTable);
        scroll.setPreferredSize(new Dimension(600, 200));
        gbc.gridy = 5;
        form.add(scroll, gbc);

        searchBtn.addActionListener(e -> {
            String name = (String) nameCombo.getSelectedItem();
            String author = (String) authorCombo.getSelectedItem();

            if ((name == null || name.isEmpty()) && (author == null || author.isEmpty())) {
                errorLabel.setText("Please select at least Book Name or Author.");
                return;
            }

            errorLabel.setText("");
            List<String[]> results = DataStore.searchBooks(
                name == null ? "" : name,
                author == null ? "" : author
            );

            Object[][] tableData = new Object[results.size()][4];
            for (int i = 0; i < results.size(); i++) {
                String[] b = results.get(i);
                tableData[i][0] = b[1];
                tableData[i][1] = b[2];
                tableData[i][2] = b[0];
                tableData[i][3] = b[4];
            }

            JTable newTable = new JTable(tableData, cols);
            scroll.setViewportView(newTable);
            form.revalidate();
            form.repaint();
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
            if (isAdmin) {
                new AdminHomeFrame(username).setVisible(true);
            } else {
                new UserHomeFrame(username).setVisible(true);
            }
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        return nav;
    }
}