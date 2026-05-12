import javax.swing.*;
import java.awt.*;

public class ReportsFrame extends JFrame {

    public ReportsFrame(String username, boolean isAdmin) {
        setTitle("Reports");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton homeBtn = new JButton("Home");
        JButton logoutBtn = new JButton("Log Out");
        nav.add(homeBtn);
        nav.add(logoutBtn);
        main.add(nav, BorderLayout.NORTH);

        JPanel sidebar = new JPanel(new GridLayout(6, 1, 5, 5));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] reportNames = {"Master List of Books", "Master List of Movies", "Master List of Memberships", "Active Issues", "Overdue Returns", "Issue Requests"};
        JButton[] sideButtons = new JButton[reportNames.length];
        for (int i = 0; i < reportNames.length; i++) {
            sideButtons[i] = new JButton(reportNames[i]);
            sidebar.add(sideButtons[i]);
        }
        main.add(sidebar, BorderLayout.WEST);

        JPanel contentPanel = new JPanel(new BorderLayout());
        JLabel contentTitle = new JLabel("Select a report from left", SwingConstants.CENTER);
        contentTitle.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(contentTitle, BorderLayout.NORTH);
        JScrollPane tableScroll = new JScrollPane(new JTable());
        contentPanel.add(tableScroll, BorderLayout.CENTER);
        main.add(contentPanel, BorderLayout.CENTER);

        sideButtons[0].addActionListener(e -> {
            contentTitle.setText("Master List of Books");
            String[] cols = {"Serial No", "Name", "Author", "Category", "Status", "Cost", "Procurement Date"};
            Object[][] data = DataStore.books.stream()
                .filter(b -> b[7].equals("Book"))
                .map(b -> new Object[]{b[0], b[1], b[2], b[3], b[4], b[5], b[6]})
                .toArray(Object[][]::new);
            tableScroll.setViewportView(new JTable(data, cols));
            contentPanel.revalidate(); contentPanel.repaint();
        });
        sideButtons[1].addActionListener(e -> {
            contentTitle.setText("Master List of Movies");
            String[] cols = {"Serial No", "Name", "Director", "Category", "Status", "Cost", "Procurement Date"};
            Object[][] data = DataStore.books.stream()
                .filter(b -> b[7].equals("Movie"))
                .map(b -> new Object[]{b[0], b[1], b[2], b[3], b[4], b[5], b[6]})
                .toArray(Object[][]::new);
            tableScroll.setViewportView(new JTable(data, cols));
            contentPanel.revalidate(); contentPanel.repaint();
        });
        sideButtons[2].addActionListener(e -> {
            contentTitle.setText("Master List of Memberships");
            String[] cols = {"Membership Id", "First Name", "Last Name", "Contact", "Address", "Aadhar", "Start", "End", "Status", "Fine"};
            Object[][] data = DataStore.memberships.stream()
                .map(m -> new Object[]{m[0], m[1], m[2], m[3], m[4], m[5], m[6], m[7], m[8], m[9]})
                .toArray(Object[][]::new);
            tableScroll.setViewportView(new JTable(data, cols));
            contentPanel.revalidate(); contentPanel.repaint();
        });
        sideButtons[3].addActionListener(e -> {
            contentTitle.setText("Active Issues");
            String[] cols = {"Serial No", "Book/Movie", "Membership Id", "Issue Date", "Return Date"};
            Object[][] data = DataStore.activeIssues.stream()
                .map(i -> new Object[]{i[0], i[1], i[2], i[3], i[4]})
                .toArray(Object[][]::new);
            tableScroll.setViewportView(new JTable(data, cols));
            contentPanel.revalidate(); contentPanel.repaint();
        });
        sideButtons[4].addActionListener(e -> {
            contentTitle.setText("Overdue Returns");
            String[] cols = {"Serial No", "Book", "Membership Id", "Issue Date", "Due Date", "Fine (Rs)"};
            java.time.LocalDate today = java.time.LocalDate.now();
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Object[][] data = DataStore.activeIssues.stream()
                .filter(i -> {
                    try { return java.time.LocalDate.parse(i[4], fmt).isBefore(today); } catch (Exception ex) { return false; }
                })
                .map(i -> new Object[]{i[0], i[1], i[2], i[3], i[4], DataStore.calculateFine(i[4], DataStore.getTodayDate())})
                .toArray(Object[][]::new);
            tableScroll.setViewportView(new JTable(data, cols));
            contentPanel.revalidate(); contentPanel.repaint();
        });
        sideButtons[5].addActionListener(e -> {
            contentTitle.setText("Issue Requests");
            String[] cols = {"Membership Id", "Book/Movie", "Requested Date", "Fulfilled Date"};
            Object[][] data = DataStore.issueRequests.stream()
                .map(r -> new Object[]{r[0], r[1], r[2], r[3]})
                .toArray(Object[][]::new);
            tableScroll.setViewportView(new JTable(data, cols));
            contentPanel.revalidate(); contentPanel.repaint();
        });

        homeBtn.addActionListener(e -> { dispose(); if (isAdmin) new AdminHomeFrame(username).setVisible(true); else new UserHomeFrame(username).setVisible(true); });
        logoutBtn.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });

        add(main);
    }
}