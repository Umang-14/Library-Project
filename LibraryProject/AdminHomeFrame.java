import javax.swing.*;
import java.awt.*;

public class AdminHomeFrame extends JFrame {

    public AdminHomeFrame(String username) {
        setTitle("Admin Home - Library Management System");
        setSize(700, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton maintenanceBtn = new JButton("Maintenance");
        JButton reportsBtn = new JButton("Reports");
        JButton transactionsBtn = new JButton("Transactions");
        JButton logoutBtn = new JButton("Log Out");
        nav.add(maintenanceBtn);
        nav.add(reportsBtn);
        nav.add(transactionsBtn);
        nav.add(Box.createHorizontalStrut(200));
        nav.add(logoutBtn);
        main.add(nav, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome Admin - " + username, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        center.add(welcomeLabel, BorderLayout.NORTH);

        String[] cols = {"Code No From", "Code No To", "Category"};
        Object[][] data = {
            {"SC(B/M)000001", "SC(B/M)000004", "Science"},
            {"EC(B/M)000001", "EC(B/M)000004", "Economics"},
            {"FC(B/M)000001", "FC(B/M)000004", "Fiction"},
            {"CH(B/M)000001", "CH(B/M)000004", "Children"},
            {"PD(B/M)000001", "PD(B/M)000004", "Personal Development"}
        };
        JTable table = new JTable(data, cols);
        table.setEnabled(false);
        center.add(new JScrollPane(table), BorderLayout.CENTER);
        main.add(center, BorderLayout.CENTER);

        maintenanceBtn.addActionListener(e -> { dispose(); new MaintenanceFrame(username, true).setVisible(true); });
        reportsBtn.addActionListener(e -> { dispose(); new ReportsFrame(username, true).setVisible(true); });
        transactionsBtn.addActionListener(e -> { dispose(); new TransactionsFrame(username, true).setVisible(true); });
        logoutBtn.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });

        add(main);
    }
}